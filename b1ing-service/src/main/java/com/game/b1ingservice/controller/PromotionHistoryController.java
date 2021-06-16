package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.promotion.PromotionHistorySearchRequest;
import com.game.b1ingservice.payload.promotion.PromotionSummaryHistorySearchResponse;
import com.game.b1ingservice.service.PromotionHistoryService;
import com.game.b1ingservice.specification.SearchPromotionHistorySpecification;
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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/admin")
@Slf4j
public class PromotionHistoryController {

    @Autowired
    private PromotionHistoryService promotionHistoryService;

    @PostMapping(value = "/search/summary/promotionHistory",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> searchSummaryPromotionHistory(@RequestBody PromotionHistorySearchRequest req, @AuthenticationPrincipal UserPrincipal principal) {
        req.setAgentId(principal.getAgentId());
        SearchPromotionHistorySpecification specification = new SearchPromotionHistorySpecification(req);

        List<PromotionSummaryHistorySearchResponse> searchResponse = promotionHistoryService.findSummaryByCriteria(specification);

        BigDecimal summary = promotionHistoryService.totalBonus(searchResponse);

        return ResponseHelper.successListWithSummary(searchResponse, summary, "data", Constants.MESSAGE.MSG_00000.msg);
    }

}
