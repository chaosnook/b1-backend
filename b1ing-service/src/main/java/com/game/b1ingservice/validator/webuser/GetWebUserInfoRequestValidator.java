package com.game.b1ingservice.validator.webuser;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.webuser.GetUserInfoRequest;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class GetWebUserInfoRequestValidator extends CommonValidator {

    public void validate(GetUserInfoRequest req) {
        if(StringUtils.isEmpty(req.getUsername())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01118);
        }
    }
}
