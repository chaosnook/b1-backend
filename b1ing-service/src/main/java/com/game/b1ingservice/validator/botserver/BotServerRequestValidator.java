package com.game.b1ingservice.validator.botserver;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.bot_server.BotServerRequest;
import com.game.b1ingservice.postgres.repository.BotServerRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotServerRequestValidator extends CommonValidator {
   // @Autowired
   // private BotServerRepository botServerRepository;

    public void validate(BotServerRequest req){
        if(StringUtils.isEmpty(req.getBotIp()))
            throw new ErrorMessageException(Constants.ERROR.ERR_02009);
        else if (!isIpAddress(req.getBotIp()))
            throw new ErrorMessageException(Constants.ERROR.ERR_02013);
        }
}
