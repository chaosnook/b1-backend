package com.game.b1ingservice.payload.bank;

import lombok.Data;

import java.time.Instant;

@Data
public class BankResponse {
    private Long id;
    private String bankCode;
    private String bankType;
    private String bankName;
    private String bankAccountName;
    private String bankAccountNo;
    private String username;
    private String password;
    private int bankOrder;
    private int bankGroup;
    private String botIp;
    private boolean newUserFlag;
    private boolean active;
    private int version;
    private Instant createdDate;
    private Instant updatedDate;
    private String createdBy;
    private String updatedBy;
}
