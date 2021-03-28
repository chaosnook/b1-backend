package com.game.b1ingservice.payload.promotion;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PromotionSummaryHistorySearchResponse {

    private String name;
    private int countPromotion;
    private BigDecimal sumBonus;
//    private BigDecimal totalBonus;

}
