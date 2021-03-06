package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.commons.UserPrincipal;
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

    @PostMapping(value = "search/list/withdrawHistory", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> searchListWithDrawHistory(@RequestBody WithdrawHistorySearchRequest req) {
        SearchWithdrawHistorySpecification specification = new SearchWithdrawHistorySpecification(req);
        Page<WithdrawListHistorySearchResponse> searchResponse = withdrawHistoryService.findByCriteria(specification, specification.getPageable(), null);
        return ResponseHelper.successPage(searchResponse, "data", Constants.MESSAGE.MSG_00000.msg);
    }

    @PostMapping(value = "search/summary/withdrawHistory", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> searchSummaryWithDrawHistory(@RequestBody WithdrawHistorySearchRequest req) {
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
    public ResponseEntity<?> updateStatus(@RequestBody WithdrawHistoryUpdateStatusReq req) {
        withdrawHistoryService.updateStatus(req);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }
}
