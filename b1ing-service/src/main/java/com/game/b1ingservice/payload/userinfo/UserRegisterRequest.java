package com.game.b1ingservice.payload.userinfo;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String tel;
    private String password;
    private String bankName;
    private String accountNumber;
    private String firstName;
    private String lastName;
    private String line;
    private String affiliate;
    private String isBonus;
}
