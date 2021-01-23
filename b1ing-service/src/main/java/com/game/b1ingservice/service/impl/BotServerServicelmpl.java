package com.game.b1ingservice.service.impl;


import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.bot_server.Bot_serverRequest;
import com.game.b1ingservice.payload.bot_server.Bot_serverResponse;
import com.game.b1ingservice.postgres.entity.Bot_server;
import com.game.b1ingservice.postgres.repository.Bot_serverRepository;
import com.game.b1ingservice.service.BotServerService;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BotServerServicelmpl implements BotServerService {
    @Autowired
    Bot_serverRepository botServerRepository;

    @Override
    public void addBot(Bot_serverRequest botServerRequest){
        Bot_server bot_server = new Bot_server();
        bot_server.setBotIp(botServerRequest.getBotIp());
        botServerRepository.save(bot_server);
    }
    @Override
    public ResponseEntity<?> getBot(){
        List<Bot_serverResponse> responseList = new ArrayList<>();
        List<Bot_server> bot_serverList = botServerRepository.findAll();
        if(bot_serverList.isEmpty()){
            return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, responseList);
        }
        for (Bot_server botServer: bot_serverList){
            Bot_serverResponse bot_serverResponse = new Bot_serverResponse();
            bot_serverResponse.setId(botServer.getId());
            bot_serverResponse.setBotIp(botServer.getBotIp());
            bot_serverResponse.setCreatedBy(botServer.getAudit().getCreatedBy());
            bot_serverResponse.setUpdatedBy(botServer.getAudit().getUpdatedBy());
            bot_serverResponse.setCreatedDate(botServer.getCreatedDate());
            bot_serverResponse.setUpdatedDate(botServer.getUpdatedDate());

            responseList.add(bot_serverResponse);
        }
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, responseList);
    }
    @Override
    public void updateBot(Long id,Bot_serverRequest botServerRequest){
        Optional<Bot_server> opt = botServerRepository.findById(id);
        if (opt.isPresent()) {
            Bot_server botServer = opt.get();
            botServer.setBotIp(botServerRequest.getBotIp());
            botServerRepository.save(botServer);
        }
        else {
            throw new ErrorMessageException(Constants.ERROR.ERR_03001);
        }
    }

    @Override
    public void deleteBot(Long id) {
      Optional<Bot_server> opt = botServerRepository.findById(id);
      if(opt.isPresent()){
          Bot_server botServer = opt.get();
          botServer.setDeleteFlag(1);
          botServerRepository.save(botServer);
      }
      else {
          throw new ErrorMessageException(Constants.ERROR.ERR_03001);
      }
    }
}
