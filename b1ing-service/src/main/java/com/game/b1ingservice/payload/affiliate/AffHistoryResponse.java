package com.game.b1ingservice.payload.affiliate;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AffHistoryResponse {
    private String username;
    private BigDecimal amount;

}
