package com.game.b1ingservice.validator.item;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.item.AddItemRequest;
import com.game.b1ingservice.postgres.repository.ItemRepository;
import com.game.b1ingservice.validator.CommonValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddItemValidator extends CommonValidator {
    @Autowired
    ItemRepository itemRepository;

    @Override
    public boolean supports(Class clazz) {
        return AddItemRequest.class.isAssignableFrom(clazz);
    }

    public void validate(Object o) {
        AddItemRequest req = AddItemRequest.class.cast(o);
        if (StringUtils.isEmpty(req.getName()))
            throw new ErrorMessageException(Constants.ERROR.ERR_10002);
//        else if (itemRepository.exists(req.getName()))
//            throw new ErrorMessageException(Constants.ERROR.ERR_10003);
        if (StringUtils.isEmpty(req.getQuantity()))
            throw new ErrorMessageException(Constants.ERROR.ERR_10004);
        if (StringUtils.isEmpty(req.getCost()))
            throw new ErrorMessageException(Constants.ERROR.ERR_10005);
        if (StringUtils.isEmpty(req.getSale()))
            throw new ErrorMessageException(Constants.ERROR.ERR_10006);
    }
}
