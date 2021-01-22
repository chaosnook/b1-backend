package com.game.b1ingservice.validator.truewallet;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.truewallet.TrueWalletRequest;
import com.game.b1ingservice.postgres.repository.TrueWalletRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrueWalletUpdateValidator extends CommonValidator {

    @Autowired
    private TrueWalletRepository trueWalletRepository;

    public void validate(TrueWalletRequest req) {
        if(StringUtils.isEmpty(req.getPhoneNumber())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01106);
        } else if (!isNumber(req.getPhoneNumber())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01114);
        }
        if(StringUtils.isEmpty(req.getPassword())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01108);
        }
        if(StringUtils.isEmpty(req.getName())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01109);
        }
        if(StringUtils.isEmpty(req.getBotIp())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01110);
        } else if(!isIpAddress(req.getBotIp())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_O1115);
        }
        if(ObjectUtils.isEmpty(req.getBankGroup())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01111);
        }
        if(ObjectUtils.isEmpty(req.isNewUserFlag())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01112);
        }
        if(ObjectUtils.isEmpty(req.isActive())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01113);
        }
    }
}

