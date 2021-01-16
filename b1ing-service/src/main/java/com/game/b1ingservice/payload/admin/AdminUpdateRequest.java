package com.game.b1ingservice.payload.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminUpdateRequest implements Serializable {
    private Long id;
    private String password;
    private String tel;
    private String fullName;
    private int limit = 0;
    private int isLimit = 0;
    private int active;
    private String prefix;
    private String roleCode;
}
