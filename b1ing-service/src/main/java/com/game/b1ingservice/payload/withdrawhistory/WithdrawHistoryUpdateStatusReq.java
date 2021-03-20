package com.game.b1ingservice.payload.withdrawhistory;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawHistoryUpdateStatusReq {

    private Long id;
    private BigDecimal amount;
    private String status;
}
