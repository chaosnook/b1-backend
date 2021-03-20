package com.game.b1ingservice.postgres.jdbc.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CountRefillDTO {
    private String username;
    private int countDeposit;
    private BigDecimal allDeposit;
    private int countWithdraw;
    private BigDecimal allWithdraw;
}
