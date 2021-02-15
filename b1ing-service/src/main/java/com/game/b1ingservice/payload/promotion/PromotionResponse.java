package com.game.b1ingservice.payload.promotion;

import lombok.Data;

@Data
public class PromotionResponse {
    private Long id;
    private String name;
    private String typePromotion ;
    private int maxBonus;
    private int minTopup;
    private int maxTopup;
    private int turnOver;
    private int maxWithdraw;
    private boolean active;
    private String urlImage;

}
