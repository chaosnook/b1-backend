package com.game.b1ingservice.validator.admin;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.admin.AdminUpdateRequest;
import com.game.b1ingservice.postgres.repository.AdminUserRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateValidator extends CommonValidator {
    @Autowired
    AdminUserRepository adminUserRepository;

    @Override
    public boolean supports(Class clazz) {
        return AdminUpdateRequest.class.isAssignableFrom(clazz);
    }

    public void validate(Object o) {
        AdminUpdateRequest req = AdminUpdateRequest.class.cast(o);
        if (ObjectUtils.isEmpty(req.getId()))
            throw new ErrorMessageException(Constants.ERROR.ERR_00010);
//        if (StringUtils.isEmpty(req.getPassword()))
//            throw new ErrorMessageException(Constants.ERROR.ERR_00005);
        if (StringUtils.isEmpty(req.getFullName()))
            throw new ErrorMessageException(Constants.ERROR.ERR_00006);
    }
}