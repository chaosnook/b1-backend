package com.game.b1ingservice.payload.webuser;

import lombok.Data;

@Data
public class WebUserRequest {
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
