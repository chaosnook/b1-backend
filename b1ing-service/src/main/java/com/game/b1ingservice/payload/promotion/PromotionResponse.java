package com.game.b1ingservice.payload.promotion;

import lombok.Data;

@Data
public class PromotionResponse {
    private Long id;
    private String name;
    private String typePromotion ;
    private Double maxBonus;
    private Double minTopup;
    private Double maxTopup;
    private Double turnOver;
    private Double maxWithdraw;
    private boolean active;
    private String urlImage;

}
