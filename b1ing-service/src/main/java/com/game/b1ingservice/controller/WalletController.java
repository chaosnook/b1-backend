package com.game.b1ingservice.controller;

import com.game.b1ingservice.payload.wellet.WalletRequest;
import com.game.b1ingservice.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/admin")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping(value = "/wallet")
    public void createWallet(@RequestBody WalletRequest req) {
        walletService.createWallet(req);
    }
}
