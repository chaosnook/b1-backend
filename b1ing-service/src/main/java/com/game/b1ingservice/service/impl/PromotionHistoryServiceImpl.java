package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.promotion.PromotionListHistorySearchResponse;
import com.game.b1ingservice.payload.promotion.PromotionSummaryHistorySearchResponse;
import com.game.b1ingservice.payload.withdrawhistory.WithdrawListHistorySearchResponse;
import com.game.b1ingservice.postgres.entity.PromotionHistory;
import com.game.b1ingservice.postgres.entity.WithdrawHistory;
import com.game.b1ingservice.postgres.repository.PromotionHistoryRepository;
import com.game.b1ingservice.service.PromotionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class PromotionHistoryServiceImpl implements PromotionHistoryService {

    @Autowired
    private PromotionHistoryRepository promotionHistoryRepository;

    @Override
    public Page<PromotionSummaryHistorySearchResponse> findSummaryByCriteria(Specification<PromotionHistory> specification, Pageable pageable){

        Page<PromotionSummaryHistorySearchResponse> searchData = promotionHistoryRepository.findAll(specification,pageable).map(converter);
        return summaryHistory(searchData.getContent(), pageable);
    }

    private Page<PromotionSummaryHistorySearchResponse> summaryHistory(List<PromotionSummaryHistorySearchResponse> searchData, Pageable pageable) {

        Map<String, PromotionSummaryHistorySearchResponse> map = new HashMap<>();

        for(PromotionSummaryHistorySearchResponse promotion: searchData) {

            PromotionSummaryHistorySearchResponse summary = new PromotionSummaryHistorySearchResponse();

//            summary.setTotalBonus(summary.getTotalBonus().add(promotion.getSumBonus()));

            if(!map.containsKey(promotion.getName())) {

                summary.setName(promotion.getName());
                summary.setCountPromotion(1);
                summary.setSumBonus(promotion.getSumBonus());
                summary.setTotalBonus(promotion.getTotalBonus());

                map.put(promotion.getName(), summary);

            }  else {

                PromotionSummaryHistorySearchResponse value = map.get(promotion.getName());
                summary.setName(value.getName());
                summary.setCountPromotion(value.getCountPromotion() + 1);
                summary.setSumBonus(value.getSumBonus().add(promotion.getSumBonus()));
                summary.setTotalBonus(value.getTotalBonus().add(promotion.getTotalBonus()));

                map.replace(promotion.getName(), summary);
            }

//                summary.setTotalBonus(promotion.getTotalBonus().add(promotion.getSumBonus()));
//                map.put(promotion.getName(), summary);
        }

        List<PromotionSummaryHistorySearchResponse> listSummary = new ArrayList<>();

        for (Map.Entry<String, PromotionSummaryHistorySearchResponse> entry : map.entrySet()) {
            listSummary.add(entry.getValue());
        }

        Page<PromotionSummaryHistorySearchResponse> searchResponse = new PageImpl<>(listSummary, pageable, listSummary.size());
        return searchResponse;
    }

    Function<PromotionHistory, PromotionSummaryHistorySearchResponse> converter = promotionHistory -> {
        PromotionSummaryHistorySearchResponse searchResponse = new PromotionSummaryHistorySearchResponse();
        if(null == promotionHistory.getPromotion()) {
            searchResponse.setName(null);

        } else {
            searchResponse.setName(promotionHistory.getPromotion().getName());
        }

        searchResponse.setSumBonus(promotionHistory.getBonus());
        searchResponse.setTotalBonus(promotionHistory.getBonus());

        return searchResponse;
    };
}
