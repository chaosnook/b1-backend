package com.game.b1ingservice.validator.promotion;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.promotion.PromotionUpdate;
import com.game.b1ingservice.postgres.entity.Promotion;
import com.game.b1ingservice.postgres.repository.PromotionRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class PromotionUpdateValidator extends CommonValidator {

    @Autowired
    PromotionRepository promotionRepository;

    @Override
    public boolean supports(Class clazz) {return PromotionUpdate.class.isAssignableFrom(clazz); }

    public void validate(Object o, Long id){

        Promotion promotion = new Promotion();
        Optional<Promotion> opt = promotionRepository.findById(id);
        if(opt.isPresent()) {
            promotion = opt.get();
        }

        PromotionUpdate req = PromotionUpdate.class.cast(o);

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

    }
}
