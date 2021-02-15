package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.bank.BankRequest;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.service.BankBotService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.bankbot.BankBotAddCreditValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/bankbot")
public class BankBotController {

    @Autowired
    private BankBotAddCreditValidator bankBotAddCreditValidator;
    @Autowired
    private BankBotService bankBotService;
    @PostMapping(value = "/addcredit", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> addCredit(@RequestBody BankBotAddCreditRequest bankRequest){
        bankBotAddCreditValidator.validate(bankRequest);
        bankBotService.addCredit(bankRequest);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
