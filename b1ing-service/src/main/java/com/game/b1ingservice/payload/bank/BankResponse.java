package com.game.b1ingservice.payload.bank;

import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.entity.TrueWallet;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

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
