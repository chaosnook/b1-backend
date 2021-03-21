package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.promotion.PromotionListHistorySearchResponse;
import com.game.b1ingservice.payload.promotion.PromotionSummaryHistorySearchResponse;
import com.game.b1ingservice.payload.withdrawhistory.WithdrawListHistorySearchResponse;
import com.game.b1ingservice.postgres.entity.PromotionHistory;
import com.game.b1ingservice.postgres.entity.WithdrawHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface PromotionHistoryService {
    Page<PromotionSummaryHistorySearchResponse> findSummaryByCriteria(Specification<PromotionHistory> specification, Pageable pageable);

}
