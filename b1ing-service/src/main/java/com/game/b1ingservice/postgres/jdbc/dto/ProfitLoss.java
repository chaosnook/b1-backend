package com.game.b1ingservice.postgres.jdbc.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProfitLoss {
    private BigDecimal deposit;
    private BigDecimal bonus;
    private BigDecimal depositBonus;
    private BigDecimal withdraw;
}
