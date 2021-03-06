package com.game.b1ingservice.payload.condition;

import lombok.Data;

@Data
public class ConditionResponse {
    private Long id;
    private Double minTopup;
    private Double maxTopup;
    private Double bonus;
    private Long agentId;
}
