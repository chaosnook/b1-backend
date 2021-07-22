package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.bot_server.BotServerRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BotServerService {
    void addBot(BotServerRequest botServerRequest, Long agentId);

    ResponseEntity<?> getBot(Long agentId);

    void updateBot(Long id, BotServerRequest botServerRequest, Long agentId);

    void deleteBot(Long id, Long agentId);
}
