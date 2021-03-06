package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.promotion.PromotionListHistorySearchResponse;
import com.game.b1ingservice.postgres.entity.PromotionHistory;
import com.game.b1ingservice.postgres.repository.PromotionHistoryRepository;
import com.game.b1ingservice.service.PromotionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PromotionHistoryServiceImpl implements PromotionHistoryService {

    @Autowired
    private PromotionHistoryRepository promotionHistoryRepository;


    @Override
    public Page<PromotionListHistorySearchResponse> findByCriteria(Specification<PromotionHistory> specification, Pageable pageable){
        return promotionHistoryRepository.findAll(specification, pageable).map(converter);

    }

    Function<PromotionHistory, PromotionListHistorySearchResponse> converter = promotionHistory -> {
        PromotionListHistorySearchResponse resp = new PromotionListHistorySearchResponse();

        resp.setId(promotionHistory.getId());
        resp.setName(promotionHistory.getPromotion().getName());
//        resp.setCountPromotion();
//        resp.setTotalBonus();
        return resp;
    };
}
