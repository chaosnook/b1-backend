package com.game.b1ingservice.payload.truewallet;

import lombok.Data;

import java.util.Map;

@Data
public class TrueWalletRequest {
    private String phoneNumber;
    private String name;
    private String password;
    private int bankGroup;
    private String botIp;
    private boolean newUserFlag;
    private boolean active;

}
