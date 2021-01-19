package com.game.b1ingservice.validator.thieve;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.thieve.ThieveUpdateRequest;
import com.game.b1ingservice.postgres.repository.ThieveRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThieveUpdateValidator extends CommonValidator {
    @Autowired
    ThieveRepository thieveRepository;

    @Override
    public boolean supports(Class clazz) {
        return ThieveUpdateRequest.class.isAssignableFrom(clazz);
    }

    public void validate(Object o) {
        ThieveUpdateRequest req = ThieveUpdateRequest.class.cast(o);
        if(ObjectUtils.isEmpty(req.getId()))
            throw new ErrorMessageException(Constants.ERROR.ERR_01000);
        if(StringUtils.isEmpty(req.getName()))
            throw new ErrorMessageException(Constants.ERROR.ERR_01001);
    }

}
