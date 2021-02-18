package com.game.b1ingservice.payload.deposithistory;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositSummaryHistorySearchResponse {

    private String BankName;
    private int countTask;
    private BigDecimal totalDeposit;
    private BigDecimal totalBonus;
}
