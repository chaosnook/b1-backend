package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.agent.AgentRequest;
import com.game.b1ingservice.payload.agent.AgentResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AgentService {
    List<AgentResponse> getAgentList();
    AgentResponse getAgentByPrefix(String prefix);

    void update(AgentRequest agentRequest, UserPrincipal principal);
}
