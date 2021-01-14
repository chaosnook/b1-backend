package com.game.b1ingservice.validator.calculator;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.calculator.CalculatorResquest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RequestValidator {

    public void validate(CalculatorResquest req) {

        if(StringUtils.isEmpty(req.getNumber1())){
            throw new ErrorMessageException(Constants.ERROR.ERR_00008);
        } else if (StringUtils.isEmpty(req.getNumber2())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00009);
        } else if (StringUtils.isEmpty(req.getOperator())) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00010);
        }
    }
}
