package com.game.b1ingservice.service.impl;


import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.bot_server.BotServerRequest;
import com.game.b1ingservice.payload.bot_server.BotServerResponse;
import com.game.b1ingservice.postgres.entity.BotServer;
import com.game.b1ingservice.postgres.repository.BotServerRepository;
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
    BotServerRepository botServerRepository;

    @Override
    public void addBot(BotServerRequest botServerRequest){
        BotServer botServer = new BotServer();
        botServer.setBotIp(botServerRequest.getBotIp());
        botServerRepository.save(botServer);
    }
    @Override
    public ResponseEntity<?> getBot(){
        List<BotServerResponse> responseList = new ArrayList<>();
        List<BotServer> bot_serverList = botServerRepository.findAll();
        if(bot_serverList.isEmpty()){
            return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, responseList);
        }
        for (BotServer botServer: bot_serverList){
            BotServerResponse bot_serverResponse = new BotServerResponse();
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
    public void updateBot(Long id,BotServerRequest botServerRequest){
        Optional<BotServer> opt = botServerRepository.findById(id);
        if (opt.isPresent()) {
            BotServer botServer = opt.get();
            botServer.setBotIp(botServerRequest.getBotIp());
            botServerRepository.save(botServer);
        }
        else {
            throw new ErrorMessageException(Constants.ERROR.ERR_03001);
        }
    }

    @Override
    public void deleteBot(Long id) {
      Optional<BotServer> opt = botServerRepository.findById(id);
      if(opt.isPresent()){
          BotServer botServer = opt.get();
          botServer.setDeleteFlag(1);
          botServerRepository.save(botServer);
      }
      else {
          throw new ErrorMessageException(Constants.ERROR.ERR_03001);
      }
    }
}
