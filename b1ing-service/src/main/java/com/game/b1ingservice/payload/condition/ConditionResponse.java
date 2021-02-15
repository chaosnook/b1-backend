package com.game.b1ingservice.payload.condition;

import lombok.Data;

@Data
public class ConditionResponse {
    private Long id;
    private int minTopup;
    private int maxTopup;
    private int bonus;
    private Long agentId;
}
