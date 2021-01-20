package com.game.b1ingservice.payload.bank;

import lombok.Data;

@Data
public class BankRequest {
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
}
