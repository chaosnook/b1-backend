package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.Url.UrlRequest;
import com.game.b1ingservice.payload.Url.UrlResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.Config;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.postgres.repository.ConfigRepository;
import com.game.b1ingservice.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService {
    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Override
    public void createUrl(UrlRequest urlRequest, Long agentId) {
        Optional<Agent> agent = agentRepository.findById(agentId);

        Config config = new Config();
        config.setParameter(urlRequest.getParameter());
        config.setType(urlRequest.getType());
        config.setValue(urlRequest.getValue());
        config.setAgent(agent.get());
        configRepository.save(config);
    }

    @Override
    public List<UrlResponse> getUrl(UserPrincipal principal) {
        Optional<Agent> agent = agentRepository.findById(principal.getAgentId());
        List<UrlResponse> responseList = new ArrayList<>();
        List<Config> configList = configRepository.findAllByTypeAndAgent(Constants.AGENT_CONFIG.URL_CONFIG, agent.get());

        for (Config config : configList) {
            UrlResponse urlResponse = new UrlResponse();
            urlResponse.setAgentId(principal.getAgentId());
            urlResponse.setParameter(config.getParameter());
            urlResponse.setValue(config.getValue());
            urlResponse.setType(config.getType());
            responseList.add(urlResponse);
        }
        return responseList;
    }

    @Override
    public void updateUrl(UrlRequest urlRequest, Long agentId) {
        Optional<Config> opt = configRepository.findById(urlRequest.getId());
        if (opt.isPresent()) {
            Config config = opt.get();
            config.setParameter(urlRequest.getParameter());
            config.setValue(urlRequest.getValue());
            config.setType(urlRequest.getType());
            configRepository.save(config);
        }

    }
}
