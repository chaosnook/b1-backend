package com.game.b1ingservice.payload.deposithistory;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class DepositListHistorySearchResponse {

    private Long id;
    private String bankName;
    private String bankCode;
    private String username;
    private BigDecimal amount;
    private BigDecimal beforeAmount;
    private BigDecimal afterAmount;
    private String type;
    private String status;
    private Boolean isAuto;
    private String reason;
    private String remark;
    private String admin;
    private BigDecimal bonus;

    private int version;
    private String createdDate;
    private String updatedDate;
    private String createdBy;
    private String updatedBy;
    private Integer deleteFlag;
}
