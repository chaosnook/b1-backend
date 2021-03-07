package com.game.b1ingservice.payload.affiliate;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AffConditionRequest {
    private BigDecimal minTopup;
    private BigDecimal maxTopup;
    private BigDecimal bonus;
}
