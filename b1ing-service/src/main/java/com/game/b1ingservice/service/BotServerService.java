package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.bot_server.Bot_serverRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BotServerService {
    void addBot(Bot_serverRequest botServerRequest);
     ResponseEntity<?> getBot();
     void updateBot(Long id,Bot_serverRequest botServerRequest);
     void  deleteBot(Long id);
}
