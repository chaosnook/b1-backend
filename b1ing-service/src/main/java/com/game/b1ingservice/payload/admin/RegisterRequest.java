package com.game.b1ingservice.payload.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterRequest implements Serializable {
    private String username;
    private String password;
    private String tel;
    private String fullName;
    private Integer limit = 0;
    private int isLimit = 0;
    private int active;
    private String prefix;
    private String roleCode;
}
