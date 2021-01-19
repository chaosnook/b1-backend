package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.bot_server.Bot_serverRequest;
import com.game.b1ingservice.service.Bot_serverService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/bot")
public class Bot_serverController {

    @Autowired
    Bot_serverService botServerService;

    @PostMapping(value = "/addbot", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> addbot(@RequestBody Bot_serverRequest botServerRequest){
        botServerService.addBot(botServerRequest);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
    @GetMapping(value = "/getbot",
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> getbot(@RequestParam("bot_id") Long id) {
        return ResponseEntity.ok(botServerService.getBot(id));
    }
}
