package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.bot_server.BotServerRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.service.BotServerService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.botserver.BotServerRequestValidator;
import com.game.b1ingservice.validator.botserver.BotServerUpdateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
public class BotServerController {

    @Autowired
    private BotServerService botServerService;
    @Autowired
    private BotServerRequestValidator botServerRequestValidator;
    @Autowired
    private BotServerUpdateValidator botServerUpdateValidator;

    @PostMapping(value = "/bot", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> addBot(@RequestBody BotServerRequest botServerRequest, @AuthenticationPrincipal UserPrincipal principal) {
        botServerRequestValidator.validate(botServerRequest);
        botServerService.addBot(botServerRequest, principal.getAgentId());
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @GetMapping(value = "/bot")
    public ResponseEntity<?> getBot(@AuthenticationPrincipal UserPrincipal principal) {
        return botServerService.getBot(principal.getAgentId());
    }

    @PutMapping(value = "/bot/{id}")
    public ResponseEntity<?> updateBot(@PathVariable Long id, @RequestBody BotServerRequest req,
                                       @AuthenticationPrincipal UserPrincipal principal) {
        botServerUpdateValidator.validate(req);
        botServerService.updateBot(id, req, principal.getAgentId());
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @DeleteMapping(value = "/bot/{id}")
    public ResponseEntity<?> deleteBot(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        botServerService.deleteBot(id, principal.getAgentId());
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
