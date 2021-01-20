package com.game.b1ingservice.validator.bot_server;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.bot_server.Bot_serverRequest;
import com.game.b1ingservice.postgres.repository.Bot_serverRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bot_serverUpdateValidator extends CommonValidator {
    @Autowired
    private Bot_serverRepository botServerRepository;

    public void validate(Bot_serverRequest req){
        if(StringUtils.isEmpty(req.getBotIp())) {
        throw new ErrorMessageException(Constants.ERROR.ERR_03000);
    }

    }
}
