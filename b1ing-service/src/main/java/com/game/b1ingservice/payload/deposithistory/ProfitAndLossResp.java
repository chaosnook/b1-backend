package com.game.b1ingservice.payload.deposithistory;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProfitAndLossResp {

    private BigDecimal deposit;
    private String bonus;
    private BigDecimal depositBonus;
    private BigDecimal withdraw;
    private BigDecimal profitAndLoss;
}
