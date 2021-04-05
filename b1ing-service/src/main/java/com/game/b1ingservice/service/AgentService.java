package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.agent.AgentRequest;
import com.game.b1ingservice.payload.agent.AgentResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.entity.Agent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AgentService {
    List<AgentResponse> getAgentList();
    AgentResponse getAgentByPrefix(String prefix);

    AgentResponse getAgentUserByPrefix(String prefix);

    List<Agent> getAllAgent();

    void update(AgentRequest agentRequest, UserPrincipal principal);

    Page<AgentResponse> findByCriteria(Specification<Agent> specification, Pageable pageable);
}
