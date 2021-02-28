package com.game.b1ingservice.payload.misktake;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MistakeSearchListRes {
    private String bankName;
    private String username;

    private BigDecimal amount;
    private BigDecimal beforeAmount;
    private BigDecimal afterAmount;

    private String status;
    private String reason;
    private String createdDate;
    private String createdBy;
    private Integer deleteFlag;

    private String mistakeType;

    private int version;
}
