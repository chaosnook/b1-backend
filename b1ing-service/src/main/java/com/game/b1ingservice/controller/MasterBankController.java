package com.game.b1ingservice.controller;

import com.game.b1ingservice.service.MasterBankService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/admin")
public class MasterBankController {

    @Autowired
    MasterBankService masterBankService;

    @GetMapping(value = "/masterbank",
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> getMasterBank() {
        return masterBankService.getMasterBank();
    }




}
