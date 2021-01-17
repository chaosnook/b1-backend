package com.game.b1ingservice.validator.thieve;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.thieve.ThieveRequest;
import com.game.b1ingservice.postgres.repository.ThieveRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThieveValidator extends CommonValidator{

    @Autowired
    ThieveRepository thieveRepository;

    @Override
    public boolean supports(Class clazz){
        return ThieveRequest.class.isAssignableFrom(clazz);
    }

    public  void getValidate(ThieveRequest req) {
//        ThieveRequest req = ThieveRequest.class.cast(o);
        if(StringUtils.isEmpty(req.getName()))
            throw new ErrorMessageException(Constants.ERROR.ERR_01001);
    }

    public  void addValidate(ThieveRequest req) {
//        ThieveRequest req = ThieveRequest.class.cast(o);
//        if(StringUtils.isEmpty(req.getId()))
//            throw new ErrorMessageException(Constants.ERROR.ERR_01000);
        if(StringUtils.isEmpty(req.getName()))
            throw new ErrorMessageException(Constants.ERROR.ERR_01001);
        if(StringUtils.isEmpty(req.getBank_name()))
            throw new ErrorMessageException(Constants.ERROR.ERR_01002);
        if(StringUtils.isEmpty(req.getBank_account()))
            throw new ErrorMessageException(Constants.ERROR.ERR_01003);
    }
}
