package com.game.b1ingservice.payload.deposithistory;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DepositHisUserRes {

    private LocalDateTime createdDate;
    private String status;
    private BigDecimal value;
    private BigDecimal bonus;
    private BigDecimal total;
    private String username;
    private String reason;

}
