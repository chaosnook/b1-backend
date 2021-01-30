package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.agent.AgentResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.walletdeposit.WalletDepositAllRequest;
import com.game.b1ingservice.payload.walletdeposit.WalletDepositRequest;
import com.game.b1ingservice.payload.walletdeposit.WalletDepositResponse;
import com.game.b1ingservice.service.WalletDepositService;
import com.game.b1ingservice.specification.SearchWalletDepositSpecification;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
public class WalletDepositController {

    private final WalletDepositService walletDepositService;

    @PostMapping(value = "/check-true-wallet-deposit")
    public ResponseEntity<?> checkWalletDeposit(@RequestBody WalletDepositRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        request = walletDepositService.checkSortField(request);
        request.setPrefix(principal.getPrefix());
        SearchWalletDepositSpecification specification = new SearchWalletDepositSpecification(request);
        Page<WalletDepositResponse> walletDep = walletDepositService.findByCriteria(specification, specification.getPageable());
        return ResponseHelper.successPage(walletDep, "datas", Constants.MESSAGE.MSG_00000.msg);
    }

    @PutMapping(value = "/update-true-wallet-deposit")
    public ResponseEntity<?> updateWalletDeposit(@RequestBody WalletDepositRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        request.setPrefix(principal.getPrefix());
        walletDepositService.updateTrueWalletDeposit(request);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }


    @PutMapping(value = "/update-all-true-wallet-deposit")
    public ResponseEntity<?> updateAllWalletDeposit(@RequestBody WalletDepositAllRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        request.setPrefix(principal.getPrefix());
        walletDepositService.updateAllTrueWalletDeposit(request);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

}
