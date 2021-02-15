package com.game.b1ingservice.validator.bankbot;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditRequest;
import com.game.b1ingservice.postgres.repository.BankRepository;
import com.game.b1ingservice.validator.CommonValidator;
import com.mchange.lang.LongUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankBotAddCreditValidator extends CommonValidator {
    @Autowired
    BankRepository bankRepository;

    @Override
    public boolean supports(Class clazz) {return BankBotAddCreditRequest.class.isAssignableFrom(clazz); }

    public void validate(Object o){
        BankBotAddCreditRequest req = BankBotAddCreditRequest.class.cast(o);
        if (StringUtils.isEmpty(req.getBotType()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09001);
//        if (StringUtils.isEmpty(req.getTransactionId()))
//            throw new ErrorMessageException(Constants.ERROR.ERR_09002);
        if (ObjectUtils.isEmpty(req.getBankId()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09003);
        if (StringUtils.isEmpty(req.getBankCode()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09004);
        if (StringUtils.isEmpty(req.getBankAccountNo()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09005);
        if (StringUtils.isEmpty(req.getAccountNo()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09006);
        if (ObjectUtils.isEmpty(req.getAmount()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09007);
        if (ObjectUtils.isEmpty(req.getTransactionDate()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09008);
        if (StringUtils.isEmpty(req.getType()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09009);
    }
}
