package com.game.b1ingservice.payload.userinfo;

import lombok.Data;

@Data
public class UserProfile {
    private String username;
    private String tel;
    private String password;
    private String bankName;
    private String accountNumber;
    private String firstName;
    private String lastName;
    private String isBonus;
    private String prefix;

    private Long userId;
    private Long agentId;

    private Boolean canWithDraw;
    private String withDrawMessage;

    private int version;
}
