package com.game.b1ingservice.payload.webuser;

import lombok.Data;

import java.time.Instant;

@Data
public class WebUserDepositList {
    private Long id;
    private String username;
    private String password;
    private String tel;
    private String bankName;
    private String accountNumber;
    private String firstName;
    private String lastName;
    private String fullName;
    private String line;
    private String recommend;
    private String isBonus;

    private int version;
    private Instant createdDate;
    private Instant updatedDate;
    private String createdBy;
    private String updatedBy;
    private Integer deleteFlag;
}
