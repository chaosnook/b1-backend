package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.MasterBank.MasterBankResponse;
import com.game.b1ingservice.service.MasterBankService;

import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/master")
public class MasterBankController {

    @Autowired
    private MasterBankService masterBankService;


    @GetMapping(value = "/bankUser",
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> getMasterBankUser() {
        List<MasterBankResponse> res = masterBankService.getMasterBankUser();
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, res);
    }

    @GetMapping(value = "/bankDeposit",
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> getMasterBankDeposit() {
        return masterBankService.getMasterBankDeposit();
    }

    @GetMapping(value = "/bankWithdraw",
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> getMasterBankWithdraw() {
        return masterBankService.getMasterBankWithdraw();
    }




}
