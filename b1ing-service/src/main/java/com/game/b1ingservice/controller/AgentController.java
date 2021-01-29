package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.agent.AgentRequest;
import com.game.b1ingservice.payload.agent.AgentResponse;
import com.game.b1ingservice.payload.agent.AgentSearchRequest;
import com.game.b1ingservice.payload.bot_server.BotServerRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.service.AgentService;
import com.game.b1ingservice.specification.SearchAgentSpecification;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.agent.AgentUpdateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AgentController {

    @Autowired
    AgentService agentService;

    @Autowired
    private AgentUpdateValidator agentUpdateValidator;

    @GetMapping(value = "/agents-list",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> authenticate(@RequestHeader Map<String, String> headers) {
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, agentService.getAgentList());
    }

    @GetMapping(value = "/agent/{prefix}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> authenticate(@RequestHeader Map<String, String> headers,
                                          @PathVariable("prefix") String prefix) {

        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, agentService.getAgentByPrefix(prefix));
    }

    @PutMapping(path = "/agent",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@RequestBody AgentRequest agentRequest, @AuthenticationPrincipal UserPrincipal principal) {
        agentUpdateValidator.validate(agentRequest, principal);
        agentService.update(agentRequest, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @PostMapping(value = "/agent/search", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> search(@RequestBody AgentSearchRequest request){
        SearchAgentSpecification specification=new SearchAgentSpecification(request);
        Page<AgentResponse> agents = agentService.findByCriteria(specification,specification.getPageable());
        return ResponseHelper.successPage(agents, "datas", Constants.MESSAGE.MSG_00000.msg);

    }
}
