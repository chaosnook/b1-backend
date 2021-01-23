package com.game.b1ingservice.validator.agent;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.agent.AgentRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
public class AgentUpdateValidator {

    public void validate(AgentRequest req, UserPrincipal principal) {
        if (ObjectUtils.isEmpty(req.getId()))
            throw new ErrorMessageException(Constants.ERROR.ERR_00010);
        if (ObjectUtils.isEmpty(req.getPrefix()))
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        if (ObjectUtils.isEmpty(principal.getRole())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        } else if (Constants.ROLE.STAFF.name().equals(principal.getRole())
                || Constants.ROLE.ADMIN.name().equals(principal.getRole())
                || !principal.getPrefix().equals(req.getPrefix()) && !Constants.ROLE.XSUPERADMIN.name().equals(principal.getRole())) {

            throw new ErrorMessageException(Constants.ERROR.ERR_88888);
        }

    }
}
