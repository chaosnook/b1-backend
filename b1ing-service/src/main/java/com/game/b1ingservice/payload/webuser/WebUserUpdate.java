package com.game.b1ingservice.payload.webuser;

import lombok.Data;

@Data
public class WebUserUpdate {
    private String bankName;
    private String accountNumber;
    private String password;
    private String firstName;
    private String lastName;
    private String line;
    private String isBonus;

}