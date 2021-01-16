package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.agent.AgentResponse;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService {
    @Autowired
    AgentRepository agentRepository;

    @Override
    public List<AgentResponse> getAgentList() {
        List<AgentResponse> list = agentRepository.findAll().stream().map(converter).collect(Collectors.toList());

        return list;
    }

    @Override
    public AgentResponse getAgentByPrefix(String prefix) {
        Optional<Agent> opt = agentRepository.findByPrefix(prefix);
        if (opt.isPresent()) {
            return converter.apply(opt.get());
        }
        throw new ErrorMessageException(Constants.ERROR.ERR_00008);
    }

    Function<Agent, AgentResponse> converter = agent -> {
        AgentResponse agentResponse = new AgentResponse();
        agentResponse.setId(agent.getId());
        agentResponse.setPrefix(agent.getPrefix());
        agentResponse.setCompanyName(agent.getCompanyName());
        agentResponse.setWebsite(agent.getWebsite());
        agentResponse.setLineId(agent.getLineId());
        agentResponse.setLogo(agent.getLogo());
        agentResponse.setBackground(agent.getBackground());
        agentResponse.setLineToken(agent.getLineToken());

        agentResponse.setCreatedDate(agent.getCreatedDate());
        agentResponse.setUpdatedDate(agent.getUpdatedDate());
        agentResponse.setCreatedBy(agent.getAudit().getCreatedBy());
        agentResponse.setUpdatedBy(agent.getAudit().getUpdatedBy());
        agentResponse.setDeleteFlag(agent.getDeleteFlag());
        agentResponse.setVersion(agent.getVersion());

        Map<String, Object> configMap = new HashMap<>();
        agent.getConfigs().forEach(config -> {
            configMap.put(config.getParameter(),config.getValue());
        });
        agentResponse.setConfig(configMap);

        return agentResponse;
    };
}
