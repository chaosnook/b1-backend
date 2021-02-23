package com.game.b1ingservice.payload.webuser;

import lombok.Data;

@Data
public class WebUserResetPasswordResponse {
    private String username;
    private String password;
}
