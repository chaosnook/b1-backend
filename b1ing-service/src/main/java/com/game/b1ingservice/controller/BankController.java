package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.bank.BankRequest;
import com.game.b1ingservice.service.BankService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.bank.BankValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class BankController {

    @Autowired
    BankValidator bankValidator;
    @Autowired
    BankService bankService;

    //CreateBank
    @PostMapping(value = "/bank", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> insertbank(@RequestBody BankRequest bankRequest){
        bankValidator.validate(bankRequest);
        bankService.insertBank(bankRequest);
        return ResponseHelper.success(Constants.MESSAGE.MSG_02000.msg);
    }

}
