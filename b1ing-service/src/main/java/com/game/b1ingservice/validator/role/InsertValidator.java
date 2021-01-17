package com.game.b1ingservice.validator.role;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.role.RoleRequest;
import com.game.b1ingservice.postgres.repository.RoleRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InsertValidator extends CommonValidator {

    @Autowired
    private RoleRepository roleRepository;

    public void validate(RoleRequest req) {
        if(StringUtils.isEmpty(req.getRoleCode())){
            throw new ErrorMessageException(Constants.ERROR.ERR_00008);
        } else if(roleRepository.existsByRoleCode(req.getRoleCode())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00010);
        }
        if(StringUtils.isEmpty(req.getDescription())){
            throw new ErrorMessageException(Constants.ERROR.ERR_00009);
        }
    }
}
