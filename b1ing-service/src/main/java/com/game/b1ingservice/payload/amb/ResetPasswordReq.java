package com.game.b1ingservice.payload.amb;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResetPasswordReq {
    private String password;
    private String signature;
}
