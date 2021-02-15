package com.game.b1ingservice.usercontroller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.bankdeposit.UserBankDepositResponse;
import com.game.b1ingservice.payload.bankdeposit.UserTrueWalletResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.userinfo.UserWalletResponse;
import com.game.b1ingservice.service.BankService;
import com.game.b1ingservice.service.WalletService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private BankService bankService;

    @GetMapping(value = "/wallet")
    public ResponseEntity<?> walletInfo(@AuthenticationPrincipal UserPrincipal principal) {
        UserWalletResponse response = walletService.getUserWallet(principal.getUsername(), principal.getPrefix());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, response);
    }

    @GetMapping(value = "/bank-deposit")
    public ResponseEntity<?> bankDeposit(@AuthenticationPrincipal UserPrincipal principal) {
        UserBankDepositResponse response = bankService.getUserBankDeposit(principal.getUsername(), principal.getPrefix());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, response);
    }

    @GetMapping(value = "/true-wallet")
    public ResponseEntity<?> trueDeposit(@AuthenticationPrincipal UserPrincipal principal) {
        UserTrueWalletResponse response = bankService.getUserTrueWallet(principal.getUsername(), principal.getPrefix());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, response);
    }
}
