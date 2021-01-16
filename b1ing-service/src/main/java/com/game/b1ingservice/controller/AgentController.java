package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.admin.LoginRequest;
import com.game.b1ingservice.service.AgentService;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AgentController {

    @Autowired
    AgentService agentService;

    @GetMapping(value = "/agent",
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
}
