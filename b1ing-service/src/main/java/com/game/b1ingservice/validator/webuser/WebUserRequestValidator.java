package com.game.b1ingservice.validator.webuser;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebUserRequestValidator extends CommonValidator {

    @Autowired
    private WebUserRepository webUserRepository;

    public void validate(WebUserRequest req) {
        if(StringUtils.isEmpty(req.getTel())){
            throw new ErrorMessageException(Constants.ERROR.ERR_01106);
        } else if(!isNumber(req.getTel())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01114);

        } else if(webUserRepository.existsByTelAndAgent_Id(req.getTel(), req.getAgentId())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01107);
        }

        if(StringUtils.isEmpty(req.getPassword())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01108);
        } else if(!isPassword(req.getPassword())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01126);
        }

        if(StringUtils.isEmpty(req.getBankName())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01120);
        }

        if(StringUtils.isEmpty(req.getAccountNumber())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01121);
        } else if(!isNumber(req.getAccountNumber())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01122);
        } else if (webUserRepository.existsByAccountNumberAndAgent_Id(req.getAccountNumber(), req.getAgentId())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01135);
        }

        if(StringUtils.isEmpty(req.getFirstName())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01123);
        }

        if(StringUtils.isEmpty(req.getLastName())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01124);
        }

        if(StringUtils.isEmpty(req.getIsBonus())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01125);
        }
    }
}
