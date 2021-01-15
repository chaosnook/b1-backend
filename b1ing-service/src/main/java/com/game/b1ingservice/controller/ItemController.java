package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.validator.item.AddItemValidator;
import com.game.b1ingservice.payload.items.AddItemRequest;
import com.game.b1ingservice.service.item.ItemService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("")

public class ItemController {

        @Autowired
        AddItemValidator addItemValidator;
        @Autowired
        ItemService itemService;

    @PostMapping(path = "/additem", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> item(@RequestBody AddItemRequest addItemRequest) {
        addItemValidator.validate(addItemRequest);
        itemService.addItem(addItemRequest);
        return ResponseHelper.success(Constants.MESSAGE.MSG_10000.msg);
    }
}
