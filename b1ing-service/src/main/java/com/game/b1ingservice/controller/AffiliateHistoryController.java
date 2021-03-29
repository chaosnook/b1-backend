package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.affiliate.AffHistoryRequest;
import com.game.b1ingservice.payload.affiliate.AffHistoryResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.jdbc.dto.SearchAffiHistoryDTO;
import com.game.b1ingservice.service.AffiliateHistoryService;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AffiliateHistoryController {

    @Autowired
    private AffiliateHistoryService affiliateHistoryService;



    @PostMapping(value = "/affiliate-history/search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> affiHistory(@RequestBody AffHistoryRequest affHistoryRequest, @AuthenticationPrincipal UserPrincipal principal) {
        List<SearchAffiHistoryDTO> obj = affiliateHistoryService.affiHistory(affHistoryRequest, principal);
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, obj);
    }

}
