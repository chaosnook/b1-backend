package com.game.b1ingservice.payload.promotion;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PromotionListHistorySearchResponse {

    private Long id;
    private String name;
    private int countPromotion;
    private BigDecimal totalBonus;
}
