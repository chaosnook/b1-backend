package com.game.b1ingservice.validator.promotion;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.condition.ConditionRequest;
import com.game.b1ingservice.payload.promotion.PromotionRequest;
import com.game.b1ingservice.postgres.entity.Condition;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PromotionValidator extends CommonValidator {

    @Override
    public boolean supports(Class clazz) {return PromotionRequest.class.isAssignableFrom(clazz); }

    public void validate(Object o){
        PromotionRequest req = PromotionRequest.class.cast(o);

        if(StringUtils.isEmpty(req.getName()))
            throw new ErrorMessageException(Constants.ERROR.ERR_06001);
        if(StringUtils.isEmpty(req.getTypeBonus()))
            throw new ErrorMessageException(Constants.ERROR.ERR_06002);
        if(StringUtils.isEmpty(req.getTypePromotion()))
            throw new ErrorMessageException(Constants.ERROR.ERR_06003);
        if(ObjectUtils.isEmpty(req.getMaxBonus()))
            throw new ErrorMessageException(Constants.ERROR.ERR_06004);
        if(ObjectUtils.isEmpty(req.getMinTopup()))
            throw new ErrorMessageException(Constants.ERROR.ERR_06005);
        if(ObjectUtils.isEmpty(req.getMaxTopup()))
            throw new ErrorMessageException(Constants.ERROR.ERR_06006);
        if(ObjectUtils.isEmpty(req.getMaxReceiveBonus()))
            throw new ErrorMessageException(Constants.ERROR.ERR_09010);
        if(ObjectUtils.isEmpty(req.getTurnOver()))
            throw new ErrorMessageException(Constants.ERROR.ERR_06007);
        if(ObjectUtils.isEmpty((req.getMaxWithdraw())))
            throw new ErrorMessageException(Constants.ERROR.ERR_06008);

        if(req.getMinTopup().compareTo(req.getMaxTopup()) == 1) {
            throw new ErrorMessageException(Constants.ERROR.ERR_12001);
        } else if (req.getMinTopup().compareTo(req.getMaxTopup()) == 0){
            throw new ErrorMessageException(Constants.ERROR.ERR_12002);
//        } else if (req.getConditions(){
//            throw new ErrorMessageException(Constants.ERROR.ERR_12003);
//        } else if (BigDecimal.valueOf(req.getConditions().indexOf(1)).compareTo(req.getMaxTopup()) == 1) {
//            throw new ErrorMessageException(Constants.ERROR.ERR_12004);
        }
    }
}
