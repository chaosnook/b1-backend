package com.game.b1ingservice.validator.item;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.admin.RegisterRequest;
import com.game.b1ingservice.payload.items.AddItemRequest;
import com.game.b1ingservice.postgres.repository.AdminUserRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddItemValidator extends CommonValidator {
//    @Autowired
//    AdminUserRepository adminUserRepository;

    @Override
    public boolean supports(Class clazz) {
        return AddItemrRequest.class.isAssignableFrom(clazz);
    }

    public void validate(Object o) {
        RegisterRequest req = RegisterRequest.class.cast(o);
        if (StringUtils.isEmpty(req.getUsername()))
            throw new ErrorMessageException(Constants.ERROR.ERR_00003);
        else if (adminUserRepository.existsByUsername(req.getUsername()))
            throw new ErrorMessageException(Constants.ERROR.ERR_00004);
        if (StringUtils.isEmpty(req.getPassword()))
            throw new ErrorMessageException(Constants.ERROR.ERR_00005);
        if (StringUtils.isEmpty(req.getFullName()))
            throw new ErrorMessageException(Constants.ERROR.ERR_00006);
    }
}
