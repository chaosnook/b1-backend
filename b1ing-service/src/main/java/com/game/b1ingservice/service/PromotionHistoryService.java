package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.promotion.PromotionListHistorySearchResponse;
import com.game.b1ingservice.postgres.entity.PromotionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface PromotionHistoryService {
    Page<PromotionListHistorySearchResponse> findByCriteria(Specification<PromotionHistory> specification, Pageable pageable);

}
