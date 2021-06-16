package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.bank.BankRequest;
import com.game.b1ingservice.payload.bank.BankResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
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

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class BankController {

    @Autowired
    private BankValidator bankValidator;

    @Autowired
    private BankUpdateValidator bankUpdateValidator;

    @Autowired
    private BankService bankService;

    //CreateBank
    @PostMapping(value = "/bank", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> insertBank(@RequestBody BankRequest bankRequest, @AuthenticationPrincipal UserPrincipal principal) {
        bankValidator.validate(bankRequest);
        bankService.insertBank(bankRequest, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_02000.msg);
    }

    //GetBank
    @GetMapping(value = "/bank",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getBank(@AuthenticationPrincipal UserPrincipal principal) {
        List<BankResponse> res = bankService.getBank(principal.getAgentId());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, res);
    }

    //UpdateBank
    @PutMapping(value = "/bank/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> updateBank(@PathVariable Long id,
                                        @RequestBody BankRequest bankRequest,
                                        @AuthenticationPrincipal UserPrincipal principal) {
        bankUpdateValidator.validate(bankRequest, id, principal.getAgentId());
        bankService.updateBank(id, bankRequest, principal.getAgentId());
        return ResponseHelper.success(Constants.MESSAGE.MSG_02001.msg);
    }

    //DeleteBank
    @DeleteMapping(value = "/bank/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> deleteBank(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        bankService.deleteBank(id, principal.getAgentId());
        return ResponseHelper.success(Constants.MESSAGE.MSG_02002.msg);
    }

}
