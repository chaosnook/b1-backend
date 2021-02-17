package com.game.b1ingservice.payload.deposithistory;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class DepositHistorySearchResponse {

    private Long id;
    private String bankName;
    private String username;
    private BigDecimal amount;
    private BigDecimal beforeAmount;
    private BigDecimal afterAmount;
    private String type;
    private String status;
    private Boolean isAuto;
    private String reason;
    private String admin;

    private int version;
    private Instant createdDate;
    private Instant updatedDate;
    private String createdBy;
    private String updatedBy;
    private Integer deleteFlag;
}
