package com.game.b1ingservice.payload.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProfitLossResponse {
    private BigDecimal deposit;
    private BigDecimal bonus;
    private BigDecimal depositBonus;
    private BigDecimal withdraw;
    private BigDecimal profitLoss;
}
