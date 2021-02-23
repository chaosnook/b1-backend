package com.game.b1ingservice.payload.deposithistory;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;

@Data
public class DepositHistoryTop20Resp {

    private BigDecimal amount;
    private BigDecimal bonus;
    private BigDecimal beforeAmount;
    private BigDecimal addCredit;
    private BigDecimal afterAmount;
    private String createdDate;
    private String reason;
}
