package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.agent.AgentRequest;
import com.game.b1ingservice.payload.agent.AgentResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.Config;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.postgres.repository.ConfigRepository;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.game.b1ingservice.commons.Constants.AGENT_CONFIG.*;
import static com.game.b1ingservice.commons.Constants.AGENT_CONFIG_STATUS;

@Service
public class AgentServiceImpl implements AgentService {
    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private WebUserRepository webUserRepository;

    @Override
    public List<AgentResponse> getAgentList() {
        return agentRepository.findAll().stream().map(converter).collect(Collectors.toList());
    }

    @Override
    public AgentResponse getAgentByPrefix(String prefix) {
        Optional<Agent> opt = agentRepository.findByPrefix(prefix);
        if (opt.isPresent()) {
            return converter.apply(opt.get());
        }
        throw new ErrorMessageException(Constants.ERROR.ERR_00008);
    }

    @Override
    public AgentResponse getAgentUserByPrefix(String prefix) {
        Optional<Agent> opt = agentRepository.findByPrefix(prefix);
        if (opt.isPresent()) {
            return converterUser.apply(opt.get());
        }
        throw new ErrorMessageException(Constants.ERROR.ERR_00008);
    }

    @Override
    public List<Agent> getAllAgent() {
        return agentRepository.findAll();
    }

    @Override
    public void update(AgentRequest agentReq, UserPrincipal principal) {
        Optional<Agent> agentOpt = agentRepository.findByPrefix(agentReq.getPrefix());
        if (agentOpt.isPresent()) {
            Agent agent = agentOpt.get();
            agent.setCompanyName(agentReq.getCompanyName());
            agent.setLineId(agentReq.getLineId());
            agent.setLineToken(agentReq.getLineToken());
            agent.setLineTokenWithdraw(agentReq.getLineTokenWithdraw());
            agent.setWebsite(agentReq.getWebsite());
            agent.setBackground(agentReq.getBackground());
            agent.setLogo(agentReq.getLogo());

            agent.setVersion(agentReq.getVersion());

            //=== save config
            List<Config> configs = agent.getConfigs();
            for (Config c : configs) {
                Object o = agentReq.getConfig().get(c.getParameter());
                if (o != null) {
                    c.setValue(String.valueOf(o));
                }
            }

            agentRepository.save(agent);

        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_00008);
        }

    }

    @Override
    public Page<AgentResponse> findByCriteria(Specification<Agent> specification, Pageable pageable) {
        return agentRepository.findAll(specification, pageable).map(converter);
    }


    @Override
    public void checkCanWithdraw(Agent agent, WebUser webUser) {
        Optional<Config> isLimit = configRepository.findFirstByParameterAndAgent(LIMIT_WITHDRAW, agent);
        if (isLimit.isPresent()) {
            boolean isCheck = Boolean.parseBoolean(isLimit.get().getValue());
            if (isCheck) {
                Optional<Config> countWithdraw = configRepository.findFirstByParameterAndAgent(COUNT_WITHDRAW, agent);
                if (countWithdraw.isPresent()) {
                    Integer max = Integer.valueOf(countWithdraw.get().getValue());
                    if (webUser.getWithdrawLimit() >= max) {
                        throw new ErrorMessageException(Constants.ERROR.ERR_01136);
                    } else {
                        webUserRepository.addCountWithdraw(webUser.getId());
                    }
                }
            }
        }
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
        agentResponse.setLineTokenWithdraw(agent.getLineTokenWithdraw());

        agentResponse.setCreatedDate(agent.getCreatedDate());
        agentResponse.setUpdatedDate(agent.getUpdatedDate());
        agentResponse.setCreatedBy(agent.getAudit().getCreatedBy());
        agentResponse.setUpdatedBy(agent.getAudit().getUpdatedBy());
        agentResponse.setDeleteFlag(agent.getDeleteFlag());
        agentResponse.setVersion(agent.getVersion());

        Map<String, Object> configMap = new HashMap<>();
        agent.getConfigs().forEach(config -> {
            if (AGENT_CONFIG_STATUS.contains(config.getParameter())) {
                configMap.put(config.getParameter(), Boolean.parseBoolean(config.getValue()));
            } else {
                configMap.put(config.getParameter(), config.getValue());
            }
        });

        agentResponse.setConfig(configMap);

        return agentResponse;
    };

    Function<Agent, AgentResponse> converterUser = agent -> {
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
            if (AGENT_CONFIG_STATUS.contains(config.getParameter())) {
                if (config.getParameter().equals(ON_OFF_WEBSITE) && !Boolean.parseBoolean(config.getValue())) {
                    throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
                }

                configMap.put(config.getParameter(), Boolean.parseBoolean(config.getValue()));
            } else {
                configMap.put(config.getParameter(), config.getValue());
            }
        });

        agentResponse.setConfig(configMap);

        return agentResponse;
    };
}
