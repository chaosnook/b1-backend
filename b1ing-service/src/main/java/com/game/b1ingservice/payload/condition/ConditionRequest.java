package com.game.b1ingservice.payload.condition;

import lombok.Data;

@Data
public class ConditionRequest {
    private int minTopup;
    private int maxTopup;
    private int bonus;
    private Long promotionId;
}
