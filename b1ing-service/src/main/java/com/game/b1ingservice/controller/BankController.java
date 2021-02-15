package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.bank.BankAllRequest;
import com.game.b1ingservice.payload.bank.BankRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.service.BankService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.bank.BankUpdateValidator;
import com.game.b1ingservice.validator.bank.BankValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class BankController {

    @Autowired
    BankValidator bankValidator;
    @Autowired
    BankUpdateValidator bankUpdateValidator;
    @Autowired
    BankService bankService;

    @Autowired
    WalletRepository walletRepository;

    //CreateBank
    @PostMapping(value = "/bank", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> insertBank(@RequestBody BankRequest bankRequest, @AuthenticationPrincipal UserPrincipal principal){
//        bankValidator.validate(bankRequest);
        bankService.insertBank(bankRequest, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_02000.msg);
    }

    //GetBank
    @GetMapping(value = "/bank",
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> getBank(){
        return bankService.getBank();
    }

    //UpdateBank
    @PutMapping(value = "/bank/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> updateBank(@PathVariable Long id, @RequestBody BankRequest bankRequest, @RequestBody BankAllRequest bankAllRequest){
        bankUpdateValidator.validate(bankRequest, id);
        bankService.updateBank(id, bankRequest);
        return ResponseHelper.success(Constants.MESSAGE.MSG_02001.msg);
    }

    //DeleteBank
    @DeleteMapping(value = "/bank/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> deleteBank(@PathVariable Long id){
        bankService.deleteBank(id);
        return ResponseHelper.success(Constants.MESSAGE.MSG_02002.msg);
    }

}
