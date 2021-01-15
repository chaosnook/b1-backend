package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.item.AddItemRequest;
import com.game.b1ingservice.validator.item.AddItemValidator;
import com.game.b1ingservice.service.item.ItemService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")

public class ItemController {

        @Autowired
        AddItemValidator addItemValidator;
        @Autowired
        ItemService itemService;

    @PostMapping(value = "/additem")
    public ResponseEntity<?> item(@RequestBody AddItemRequest addItemRequest) {
        addItemValidator.validate(addItemRequest);
        itemService.addItem(addItemRequest);
        return ResponseHelper.success(Constants.MESSAGE.MSG_10000.msg);
    }
}
