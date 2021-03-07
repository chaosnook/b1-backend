package com.game.b1ingservice.validator.affiliate;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.affiliate.AffConditionRequest;
import com.game.b1ingservice.payload.affiliate.AffiliateDTO;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import org.springframework.stereotype.Component;

@Component
public class AffiliateValidator {

    public void validate(AffiliateDTO request, UserPrincipal principal) {

        for (AffConditionRequest affCon : request.getConditions()) {
            if (affCon.getBonus() == null) {
                throw new ErrorMessageException(Constants.ERROR.ERR_11001);
            } else if (affCon.getMaxTopup() == null) {
                throw new ErrorMessageException(Constants.ERROR.ERR_11002);
            } else if (affCon.getMinTopup() == null) {
                throw new ErrorMessageException(Constants.ERROR.ERR_11003);
            }
        }

    }
}
