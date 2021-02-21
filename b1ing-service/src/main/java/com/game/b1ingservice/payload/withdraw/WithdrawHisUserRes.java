package com.game.b1ingservice.payload.withdraw;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class WithdrawHisUserRes {

    private Instant createdDate;
    private String status;
    private BigDecimal value;
    private BigDecimal bonus;
    private String username;
    private String reason;

}
