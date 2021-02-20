package com.game.b1ingservice.validator.bankbot;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditRequest;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditTrueWalletRequest;
import com.game.b1ingservice.postgres.repository.BankRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankBotAddCreditTrueValidator extends CommonValidator {
    @Autowired
    BankRepository bankRepository;

    @Override
    public boolean supports(Class clazz) {return BankBotAddCreditTrueWalletRequest.class.isAssignableFrom(clazz); }

    public void validate(Object o){
        BankBotAddCreditTrueWalletRequest req = BankBotAddCreditTrueWalletRequest.class.cast(o);
        if (StringUtils.isEmpty(req.getBotType()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09001);
//        if (StringUtils.isEmpty(req.getTransactionId()))
//            throw new ErrorMessageException(Constants.ERROR.ERR_09002);
        if (ObjectUtils.isEmpty(req.getBotIp()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09003);
        if (ObjectUtils.isEmpty(req.getAmount()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09007);
        if (ObjectUtils.isEmpty(req.getTransactionDate()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09008);
        if (StringUtils.isEmpty(req.getType()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09009);
    }
}
