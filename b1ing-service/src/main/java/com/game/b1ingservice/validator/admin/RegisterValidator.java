package com.game.b1ingservice.validator.admin;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.auth.SignUpRequest;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class RegisterValidator extends CommonValidator {


    @Override
    public boolean supports(Class clazz) {
        return SignUpRequest.class.isAssignableFrom(clazz);
    }

    public void validate(Object o) {
        SignUpRequest req = SignUpRequest.class.cast(o);
        if (StringUtils.isEmpty(req.getUsername())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00003);
        }
        if (StringUtils.isEmpty(req.getPassword())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00004);
        }
        if (StringUtils.isEmpty(req.getFullName())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00005);
        }
    }
}