package com.game.b1ingservice.validator.bank;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.bank.BankRequest;
import com.game.b1ingservice.postgres.repository.BankRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankValidator extends CommonValidator {
    @Autowired
    BankRepository bankRepository;

    @Override
    public boolean supports(Class clazz) {return BankRequest.class.isAssignableFrom(clazz); }

    public void validate(Object o){
        BankRequest req = BankRequest.class.cast(o);
        if(StringUtils.isEmpty(req.getBankCode()))
            throw new ErrorMessageException(Constants.ERROR.ERR_02000);
        else if (bankRepository.existsByBankCode(req.getBankCode()));
        if(StringUtils.isEmpty(req.getBankType()))
            throw new ErrorMessageException(Constants.ERROR.ERR_02001);
        if(StringUtils.isEmpty(req.getBankName()))
            throw new ErrorMessageException(Constants.ERROR.ERR_02002);
        if(StringUtils.isEmpty(req.getBankAccountName()))
            throw new ErrorMessageException(Constants.ERROR.ERR_02003);
        if(StringUtils.isEmpty(req.getBankAccountNo()))
            throw new ErrorMessageException(Constants.ERROR.ERR_02004);
        if(StringUtils.isEmpty(req.getUsername()))
            throw new ErrorMessageException(Constants.ERROR.ERR_02005);
        if(StringUtils.isEmpty((req.getPassword())))
            throw new ErrorMessageException(Constants.ERROR.ERR_02006);
        if(ObjectUtils.isEmpty(req.getBankOrder()))
            throw new ErrorMessageException(Constants.ERROR.ERR_02007);
        if(ObjectUtils.isEmpty(req.getBankGroup()))
            throw new ErrorMessageException(Constants.ERROR.ERR_02008);
        if(StringUtils.isEmpty(req.getBotIp()))
            throw new ErrorMessageException(Constants.ERROR.ERR_02009);

    }
}
