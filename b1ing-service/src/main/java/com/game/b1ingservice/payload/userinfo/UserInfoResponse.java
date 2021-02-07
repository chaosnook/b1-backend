package com.game.b1ingservice.payload.userinfo;

import lombok.Data;

@Data
public class UserInfoResponse {
    private String token;
    private UserProfile profile;
}
