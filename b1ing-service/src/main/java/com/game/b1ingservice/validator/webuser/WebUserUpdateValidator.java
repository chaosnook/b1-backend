package com.game.b1ingservice.validator.webuser;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.webuser.WebUserUpdate;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebUserUpdateValidator extends CommonValidator {

    @Autowired
    private WebUserRepository webUserRepository;

    public void validate(WebUserUpdate req) {

        if(StringUtils.isEmpty(req.getUsername())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01118);
        }

        if(StringUtils.isEmpty(req.getBankName())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01120);
        }

        if(StringUtils.isEmpty(req.getAccountNumber())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01121);
        } else if(!isNumber(req.getAccountNumber())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01122);
        }

        if(StringUtils.isEmpty(req.getFirstName())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01123);
        }

        if(StringUtils.isEmpty(req.getLastName())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01124);
        }

        if(StringUtils.isEmpty(req.getTel())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01106);
        }

        if(StringUtils.isEmpty(req.getIsBonus())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01125);
        }
    }
}
