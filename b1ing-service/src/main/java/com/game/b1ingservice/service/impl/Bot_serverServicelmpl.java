package com.game.b1ingservice.service.impl;


import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.bot_server.Bot_serverRequest;
import com.game.b1ingservice.payload.bot_server.Bot_serverResponse;
import com.game.b1ingservice.postgres.entity.Bot_server;
import com.game.b1ingservice.postgres.repository.Bot_serverRepository;
import com.game.b1ingservice.service.Bot_serverService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class Bot_serverServicelmpl implements Bot_serverService {
    @Autowired
    Bot_serverRepository botServerRepository;

    @Override
    public void addBot(Bot_serverRequest botServerRequest){
        Bot_server bot_server = new Bot_server();
        bot_server.setBotId(botServerRequest.getBotId());
        botServerRepository.save(bot_server);
    }
    @Override
    public ResponseEntity<?> getBot(Long id) {
        Optional<Bot_server> opt = botServerRepository.findById(id);
        if(opt.isPresent()){
            Bot_server botServer = opt.get();
            Bot_serverResponse response = new Bot_serverResponse();
            response.setBotId(botServer.getBotId());
            return ResponseEntity.ok(opt.get());
        }
        throw new ErrorMessageException(Constants.ERROR.ERR_01004);
    }
}
