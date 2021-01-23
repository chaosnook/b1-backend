package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.bot_server.BotServerRequest;
import com.game.b1ingservice.service.BotServerService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.botserver.BotServerRequestValidator;
import com.game.b1ingservice.validator.botserver.BotServerUpdateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/test")
public class BotServerController {

    @Autowired
    BotServerService botServerService;
    @Autowired
    BotServerRequestValidator botServerRequestValidator;
    @Autowired
    BotServerUpdateValidator botServerUpdateValidator;

    @PostMapping(value = "/bot", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> addBot(@RequestBody BotServerRequest botServerRequest){
        botServerRequestValidator.validate(botServerRequest);
        botServerService.addBot(botServerRequest);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
    @GetMapping(value = "/bot")
    public ResponseEntity<?> getBot() {
        return botServerService.getBot();
    }
    @PutMapping(value = "/bot/{id}")
    public ResponseEntity<?> updateBot(@PathVariable Long id, @RequestBody BotServerRequest req){
        botServerUpdateValidator.validate(req);
        botServerService.updateBot(id, req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
    @DeleteMapping(value = "/bot/{id}")
    public ResponseEntity<?> deleteBot(@PathVariable Long id) {
        botServerService.deleteBot(id);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
