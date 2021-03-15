package com.game.b1ingservice.validator.condition;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.condition.ConditionRequest;
import com.game.b1ingservice.payload.promotion.PromotionRequest;
import com.game.b1ingservice.postgres.entity.Condition;
import com.game.b1ingservice.postgres.repository.ConditionRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConditionValidator extends CommonValidator {

    @Autowired
    ConditionRepository conditionRepository;

    @Override
    public boolean supports(Class clazz) {
        return ConditionRequest.class.isAssignableFrom(clazz);
    }

    public void validate(Object o) {
        ConditionRequest req = ConditionRequest.class.cast(o);

        if (req.getMinTopup().compareTo(req.getMaxTopup()) == 1) {
            throw new ErrorMessageException(Constants.ERROR.ERR_12001);
        } else if (req.getMinTopup().compareTo(req.getMaxTopup()) == 0) {
            throw new ErrorMessageException(Constants.ERROR.ERR_12002);
        }
    }
}
