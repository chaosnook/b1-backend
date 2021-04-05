package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.promotion.PromotionSummaryHistorySearchResponse;
import com.game.b1ingservice.postgres.entity.PromotionHistory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface PromotionHistoryService {
    List<PromotionSummaryHistorySearchResponse> findSummaryByCriteria(Specification<PromotionHistory> specification);

    BigDecimal totalBonus(List<PromotionSummaryHistorySearchResponse> page);
}
