package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.service.ItemService.AddItemService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        AddItemService addItemService;

    @GetMapping(value = "/additem",
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> authenticate(HttpServletRequest request, @RequestHeader Map<String, String> headers) {
        String basic = request.getHeader("Authorization");
        String base64 = basic.substring(6);
        String decode = new String(Base64.decodeBase64(base64.getBytes(Charset.forName("US-ASCII"))));
        String[] arr = decode.split(":");
        if (arr.length==5)
            return AddItemService.addItem(arr[0],arr[1],arr[2],arr[3],arr[4]);
        else
            return ResponseHelper.bad(Constants.ERROR.ERR_00007.msg);
}
