package com.game.b1ingservice.validator.admin;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import org.springframework.stereotype.Component;

@Component
public class PrefixValidator {

    public void validate(String prefix, UserPrincipal principal) {
        if (!principal.getPrefix().equals(prefix))
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
    }
}
