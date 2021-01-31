package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.truewallet.TrueWalletRequest;
import com.game.b1ingservice.service.TrueWalletService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.truewallet.TrueWalletRequestValidator;
import com.game.b1ingservice.validator.truewallet.TrueWalletUpdateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/admin")
public class TrueWalletController {

    @Autowired
    private TrueWalletRequestValidator trueWalletRequestValidator;

    @Autowired
    private TrueWalletService trueWalletService;

    @Autowired
    private TrueWalletUpdateValidator trueWalletUpdateValidator;

    @PostMapping(value = "/truewallet", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<?> insertTrueWallet(@RequestBody TrueWalletRequest req) {
        trueWalletRequestValidator.validate(req);
        trueWalletService.insertTrueWallet(req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @GetMapping(value = "/truewallet")
    public ResponseEntity<?> getTrueWallet() {
        return trueWalletService.getTrueWallet();
    }

    @PutMapping(value = "/truewallet/{id}")
    public ResponseEntity<?> updateTrueWallet(@PathVariable Long id, @RequestBody TrueWalletRequest req) {
        trueWalletUpdateValidator.validate(req, id);
        trueWalletService.updateTrueWallet(id, req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @DeleteMapping(value = "/truewallet/{id}")
    public ResponseEntity<?> deleteTrueWallet(@PathVariable Long id) {
        trueWalletService.deleteTrueWallet(id);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

}

