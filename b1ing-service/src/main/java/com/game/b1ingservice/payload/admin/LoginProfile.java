package com.game.b1ingservice.payload.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginProfile implements Serializable {
    private String role;
    private String username;
    private String fullName;
    private String prefix;
}
