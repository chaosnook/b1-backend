package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.withdraw.WithDrawResponse;
import com.game.b1ingservice.payload.withdrawhistory.*;
import com.game.b1ingservice.service.WithdrawHistoryService;
import com.game.b1ingservice.specification.SearchWithdrawHistorySpecification;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class WithdrawHistoryController {

    @Autowired
    private WithdrawHistoryService withdrawHistoryService;

    @PostMapping(value = "/withdrawHistory/updateBlockAutoTransaction", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> updateBlockAutoTransaction(@RequestBody WithdrawBlockStatusReq req, @AuthenticationPrincipal UserPrincipal principal) {

        WithDrawResponse response = withdrawHistoryService.updateBlockAutoTransaction(req, principal.getUsername(), principal.getAgentId());

        if (response.getStatus()) {
            return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
        } else {
            return ResponseHelper.bad(response.getMessage());
        }
    }

    @PostMapping(value = "/withdrawHistory/last20", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> withdrawHistoryLast20(@AuthenticationPrincipal UserPrincipal principal) {
        List<WithdrawHistoryTopAll20Resp> result = withdrawHistoryService.findLast20Transaction(principal.getAgentId());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, result);
    }


    @PostMapping(value = "search/list/withdrawHistory", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> searchListWithDrawHistory(@RequestBody WithdrawHistorySearchRequest req, @AuthenticationPrincipal UserPrincipal principal) {

        req.setAgentId(principal.getAgentId());
        SearchWithdrawHistorySpecification specification = new SearchWithdrawHistorySpecification(req);
        Page<WithdrawListHistorySearchResponse> searchResponse = withdrawHistoryService.findByCriteria(specification, specification.getPageable(), null);
        return ResponseHelper.successPage(searchResponse, "data", Constants.MESSAGE.MSG_00000.msg);
    }

    @PostMapping(value = "search/summary/withdrawHistory", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> searchSummaryWithDrawHistory(@RequestBody WithdrawHistorySearchRequest req, @AuthenticationPrincipal UserPrincipal principal) {

        req.setAgentId(principal.getAgentId());
        SearchWithdrawHistorySpecification specification = new SearchWithdrawHistorySpecification(req);
        Page<WithdrawSummaryHistorySearchResponse> searchResponse = withdrawHistoryService.findSummaryByCriteria(specification, specification.getPageable(), null);
        return ResponseHelper.successPage(searchResponse, "data", Constants.MESSAGE.MSG_00000.msg);
    }

    @PostMapping(value = "/withdrawHistory/userId/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> withdrawHistory(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {

        List<WithdrawHistoryByUserIdResp> result = withdrawHistoryService.findListByUserId(id, principal);
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, result);
    }

    @PutMapping(value = "/withdrawHistory/updateStatus", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> updateStatus(@RequestBody WithdrawHistoryUpdateStatusReq req, @AuthenticationPrincipal UserPrincipal principal) {
        withdrawHistoryService.updateStatus(req, principal.getAgentId());
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
