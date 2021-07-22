package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.bankdeposit.BankDepositAllRequest;
import com.game.b1ingservice.payload.bankdeposit.BankDepositList;
import com.game.b1ingservice.payload.bankdeposit.BankDepositRequest;
import com.game.b1ingservice.payload.bankdeposit.BankDepositResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.service.BankDepositService;
import com.game.b1ingservice.specification.SearchBankDepositSpecification;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
public class BankDepositController {

    private final BankDepositService bankDepositService;

    @GetMapping(value = "/list-active-bank-deposit")
    public ResponseEntity<?> listActiveBankDeposit(@AuthenticationPrincipal UserPrincipal principal) {
        List<BankDepositList> res = bankDepositService.listActiveBank(principal.getAgentId());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, res);
    }

    @GetMapping(value = "/list-usage-bank-deposit")
    public ResponseEntity<?> listUsageBankDeposit(@AuthenticationPrincipal UserPrincipal principal) {
        List<BankDepositList> res = bankDepositService.listUsageBank(principal.getAgentId());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, res);
    }

    @PostMapping(value = "/check-bank-wallet-deposit")
    public ResponseEntity<?> checkBankDeposit(@RequestBody BankDepositRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        request = bankDepositService.checkSortField(request);
        request.setPrefix(principal.getPrefix());

        SearchBankDepositSpecification specification = new SearchBankDepositSpecification(request);
        Page<BankDepositResponse> walletDep = bankDepositService.findByCriteria(specification, specification.getPageable());
        return ResponseHelper.successPage(walletDep, "datas", Constants.MESSAGE.MSG_00000.msg);
    }

    @PutMapping(value = "/update-bank-wallet-deposit")
    public ResponseEntity<?> updateBankDeposit(@RequestBody BankDepositRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        request.setPrefix(principal.getPrefix());
        bankDepositService.updateBankDeposit(request, principal.getAgentId());
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @PutMapping(value = "/update-all-bank-wallet-deposit")
    public ResponseEntity<?> updateAllBankDeposit(@RequestBody BankDepositAllRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        request.setPrefix(principal.getPrefix());
        bankDepositService.updateAllBankDeposit(request, principal.getAgentId());
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

}
