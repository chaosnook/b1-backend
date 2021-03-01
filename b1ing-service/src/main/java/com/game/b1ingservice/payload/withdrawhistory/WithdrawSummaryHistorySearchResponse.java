package com.game.b1ingservice.payload.withdrawhistory;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawSummaryHistorySearchResponse {

    private String bankName;
    private String bankGroup;
    private int countTask;
    private BigDecimal totalWithdraw;
    private String bankCode;
}
