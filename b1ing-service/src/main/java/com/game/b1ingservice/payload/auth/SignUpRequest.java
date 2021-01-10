package com.game.b1ingservice.payload.auth;

import lombok.Data;

import java.io.Serializable;

@Data
public class SignUpRequest implements Serializable {
    private String username;
    private String password;

}
