package com.game.b1ingservice.validator.admin;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.admin.AddCreditRequest;
import com.game.b1ingservice.payload.admin.WithdrawRequest;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WithdrawValidator extends CommonValidator {

    public void validate(WithdrawRequest req){
        BigDecimal minCredit = new BigDecimal(1);
        if(StringUtils.isEmpty(req.getUsername())) {
            throw  new ErrorMessageException(Constants.ERROR.ERR_01118);
        }
        if(ObjectUtils.isEmpty(req.getCredit())) {
            throw  new ErrorMessageException(Constants.ERROR.ERR_01128);
        } else if(isNumber(req.getCredit().toString())){
            throw  new ErrorMessageException(Constants.ERROR.ERR_01131);
        } else if(req.getCredit().compareTo(minCredit) == -1) {
            throw  new ErrorMessageException(Constants.ERROR.ERR_01131);
        }
    }
}
