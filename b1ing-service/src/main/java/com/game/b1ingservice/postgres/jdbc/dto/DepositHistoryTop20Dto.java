package com.game.b1ingservice.postgres.jdbc.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class DepositHistoryTop20Dto {

    private BigDecimal amount;
    private BigDecimal bonus;
    private BigDecimal beforeAmount;
    private BigDecimal addCredit;
    private BigDecimal afterAmount;
    private Timestamp createdDate;
    private String reason;
}
