package com.game.b1ingservice.validator.role;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.role.RoleUpdateRequest;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

@Component
public class UpdateRequestValidator extends CommonValidator {

    public void validate(RoleUpdateRequest req) {
        if(ObjectUtils.isEmpty(req.getId())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01105);
        }
        if(StringUtils.isEmpty(req.getRoleCode())){
            throw new ErrorMessageException(Constants.ERROR.ERR_01101);
        }
        if(StringUtils.isEmpty(req.getDescription())){
            throw new ErrorMessageException(Constants.ERROR.ERR_01102);
        }
    }
}
