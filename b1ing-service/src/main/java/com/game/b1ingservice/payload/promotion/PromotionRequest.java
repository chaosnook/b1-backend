package com.game.b1ingservice.payload.promotion;

import com.game.b1ingservice.payload.condition.ConditionRequest;
import lombok.Data;

import java.util.List;

@Data
public class PromotionRequest {
    private String name;
    private String type;
    private String typeBonus ;
    private String typePromotion ;
    private int maxBonus;
    private int minTopup;
    private int maxTopup;
    private int maxReceiveBonus;
    private int turnOver;
    private int maxWithdraw;
    private boolean active;
    private String urlImage;
    List<ConditionRequest> conditions ;
}
