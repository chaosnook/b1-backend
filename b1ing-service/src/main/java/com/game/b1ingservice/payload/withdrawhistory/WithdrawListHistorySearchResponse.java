package com.game.b1ingservice.payload.withdrawhistory;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawListHistorySearchResponse {

    private Long id;
    private String bankName;
    private String username;
    private BigDecimal amount;
    private BigDecimal beforeAmount;
    private BigDecimal afterAmount;
    private String type;
    private String status;
    private int isAuto;
    private String reason;
    private String admin;

    private int version;
    private String createdDate;
    private String updatedDate;
    private String createdBy;
    private String updatedBy;
    private Integer deleteFlag;
}
