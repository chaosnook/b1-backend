package com.game.b1ingservice.validator.wallet;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.wallet.WalletRequest;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
public class WalletRequestValidator extends CommonValidator {

    public void validate(WalletRequest req) {
        if(ObjectUtils.isEmpty(req.getCredit())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01128);
        }
        if(ObjectUtils.isEmpty(req.getPoint())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01129);
        }
    }
}
