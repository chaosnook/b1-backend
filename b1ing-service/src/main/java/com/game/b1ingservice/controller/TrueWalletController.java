package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.truewallet.TrueWalletRequest;
import com.game.b1ingservice.service.TrueWalletService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.truewallet.TrueWalletRequestValidator;
import com.game.b1ingservice.validator.truewallet.TrueWalletUpdateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping(value = "/truewallet", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> insertTrueWallet(@RequestBody TrueWalletRequest req,
                                              @AuthenticationPrincipal UserPrincipal principal) {
        trueWalletRequestValidator.validate(req);
        trueWalletService.insertTrueWallet(req, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @GetMapping(value = "/truewallet")
    public ResponseEntity<?> getTrueWallet(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, trueWalletService.getTrueWallet(principal));
    }

    @PutMapping(value = "/truewallet/{id}")
    public ResponseEntity<?> updateTrueWallet(@PathVariable Long id, @RequestBody TrueWalletRequest req
            , @AuthenticationPrincipal UserPrincipal principal) {
        trueWalletUpdateValidator.validate(req, id);
        trueWalletService.updateTrueWallet(id, req, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @DeleteMapping(value = "/truewallet/{id}")
    public ResponseEntity<?> deleteTrueWallet(@PathVariable Long id,
                                              @AuthenticationPrincipal UserPrincipal principal) {
        trueWalletService.deleteTrueWallet(id, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

}

