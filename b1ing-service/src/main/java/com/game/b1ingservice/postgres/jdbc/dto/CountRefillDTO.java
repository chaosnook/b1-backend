package com.game.b1ingservice.postgres.jdbc.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CountRefillDTO {
    private Long id;
    private String username;
    private Integer depositCount;
    private BigDecimal Deposit;
    private Integer withdrawCount;
    private BigDecimal Withdraw;
    private BigDecimal profitLoss;

}
