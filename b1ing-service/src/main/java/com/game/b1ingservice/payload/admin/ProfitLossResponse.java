package com.game.b1ingservice.payload.admin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProfitLossResponse {
    private BigDecimal deposit;
    private BigDecimal bonus;
    private BigDecimal depositBonus;
    private BigDecimal withdraw;
    private BigDecimal profitLoss;
}
