package com.game.b1ingservice.payload.promotion;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.game.b1ingservice.payload.condition.ConditionResponse;
import lombok.Data;

import java.util.Date;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone= "Asia/Bangkok")
    private Date startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone= "Asia/Bangkok")
    private Date endTime;

    List<ConditionResponse> conditions ;

}
