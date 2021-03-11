package com.game.b1ingservice.payload.promotion;

import com.game.b1ingservice.payload.condition.ConditionResponse;
import com.game.b1ingservice.postgres.entity.Agent;
import lombok.Data;

import java.util.List;

@Data
public class PromotionResponse {
    private Long id;
    private String name;
    private String type;
    private String typeBonus;
    private String typePromotion ;
    private Double maxBonus;
    private Double minTopup;
    private Double maxTopup;
    private Double maxReceiveBonus;
    private Double turnOver;
    private Double maxWithdraw;
    private boolean active;
    private String urlImage;
    private Long agentId;
    private Long adminId;
    List<ConditionResponse> conditions ;

}
