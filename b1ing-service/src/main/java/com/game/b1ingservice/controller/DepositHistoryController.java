package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.deposithistory.DepositHistorySearchRequest;
import com.game.b1ingservice.payload.deposithistory.DepositListHistorySearchResponse;
import com.game.b1ingservice.payload.deposithistory.DepositSummaryHistorySearchResponse;
import com.game.b1ingservice.service.DepositHistoryService;
import com.game.b1ingservice.specification.SearchDepositHistorySpecification;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class DepositHistoryController {

    @Autowired
    private DepositHistoryService depositHistoryService;

    @PostMapping(value = "/search/list/depositHistory", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> searchListDepositHistory(@RequestBody DepositHistorySearchRequest req){
        SearchDepositHistorySpecification specification = new SearchDepositHistorySpecification(req);
        Page<DepositListHistorySearchResponse> searchResponse = depositHistoryService.findByCriteria(specification, specification.getPageable(), req.getType());
        return ResponseHelper.successPage(searchResponse, "data", Constants.MESSAGE.MSG_00000.msg);
    }

    @PostMapping(value = "/search/summary/depositHistory", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> searchSummaryDepositHistory(@RequestBody DepositHistorySearchRequest req){
        SearchDepositHistorySpecification specification = new SearchDepositHistorySpecification(req);
        Page<DepositSummaryHistorySearchResponse> searchResponse = depositHistoryService.findSummaryByCriteria(specification, specification.getPageable(), req.getType());
        return ResponseHelper.successPage(searchResponse, "data", Constants.MESSAGE.MSG_00000.msg);
    }
}
