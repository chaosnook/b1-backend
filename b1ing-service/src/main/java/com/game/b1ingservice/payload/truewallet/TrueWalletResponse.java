package com.game.b1ingservice.payload.truewallet;

import lombok.Data;

import java.time.Instant;

@Data
public class TrueWalletResponse {
    private Long id;
    private String phoneNumber;
    private String name;
    private String password;
    private int bankGroup;
    private String botIp;
    private boolean newUserFlag;
    private boolean active;
    private String prefix;
    private int version;
    private Instant createdDate;
    private Instant updatedDate;
    private String createdBy;
    private String updatedBy;
}
