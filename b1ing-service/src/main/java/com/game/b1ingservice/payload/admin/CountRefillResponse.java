package com.game.b1ingservice.payload.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CountRefillResponse {
    private String username;
    private int depositCount;
    private BigDecimal Deposit;
    private int withdrawCount;
    private BigDecimal Withdraw;
    private BigDecimal profitLoss;
}
