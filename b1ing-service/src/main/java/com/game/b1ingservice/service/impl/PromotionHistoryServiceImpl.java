package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.promotion.PromotionSummaryHistorySearchResponse;
import com.game.b1ingservice.postgres.entity.PromotionHistory;
import com.game.b1ingservice.postgres.repository.PromotionHistoryRepository;
import com.game.b1ingservice.service.PromotionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PromotionHistoryServiceImpl implements PromotionHistoryService {

    @Autowired
    private PromotionHistoryRepository promotionHistoryRepository;

    @Override
    public List<PromotionSummaryHistorySearchResponse> findSummaryByCriteria(Specification<PromotionHistory> specification){

        List<PromotionSummaryHistorySearchResponse> searchData = promotionHistoryRepository.findAll(specification).stream().map(converter).collect(Collectors.toList());
        return summaryHistory(searchData);
    }

    private List<PromotionSummaryHistorySearchResponse> summaryHistory(List<PromotionSummaryHistorySearchResponse> searchData) {

        Map<String, PromotionSummaryHistorySearchResponse> map = new HashMap<>();

        for(PromotionSummaryHistorySearchResponse promotion: searchData) {

            PromotionSummaryHistorySearchResponse summary = new PromotionSummaryHistorySearchResponse();

            if(!map.containsKey(promotion.getName())) {

                summary.setName(promotion.getName());
                summary.setCountPromotion(1);
                summary.setSumBonus(promotion.getSumBonus());

                map.put(promotion.getName(), summary);

            }  else {

                PromotionSummaryHistorySearchResponse value = map.get(promotion.getName());
                summary.setName(value.getName());
                summary.setCountPromotion(value.getCountPromotion() + 1);
                summary.setSumBonus(value.getSumBonus().add(promotion.getSumBonus()));

                map.replace(promotion.getName(), summary);
            }

        }

        List<PromotionSummaryHistorySearchResponse> listSummary = new ArrayList<>();

        for (Map.Entry<String, PromotionSummaryHistorySearchResponse> entry : map.entrySet()) {
            listSummary.add(entry.getValue());
        }


        return listSummary;
    }

    @Override
    public BigDecimal totalBonus(List<PromotionSummaryHistorySearchResponse> list) {
        BigDecimal sum = BigDecimal.ZERO;
        for(PromotionSummaryHistorySearchResponse total : list) {
            sum = sum.add(total.getSumBonus());
        }
        return sum;
    }

    Function<PromotionHistory, PromotionSummaryHistorySearchResponse> converter = promotionHistory -> {
        PromotionSummaryHistorySearchResponse searchResponse = new PromotionSummaryHistorySearchResponse();
        if(null == promotionHistory.getPromotion()) {
            searchResponse.setName(null);

        } else {
            searchResponse.setName(promotionHistory.getPromotion().getName());
        }

        searchResponse.setSumBonus(promotionHistory.getBonus());

        return searchResponse;
    };
}
