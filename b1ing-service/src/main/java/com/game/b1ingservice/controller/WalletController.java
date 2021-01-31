package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.wellet.WalletRequest;
import com.game.b1ingservice.service.WalletService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.wallet.WalletRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/admin")
public class WalletController {

    @Autowired
    private WalletRequestValidator walletRequestValidator;

    @Autowired
    private WalletService walletService;

    @PostMapping(value = "/wallet")
    public ResponseEntity<?> createWallet(@RequestBody WalletRequest req) {
        walletRequestValidator.validate(req);
        walletService.createWallet(req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
