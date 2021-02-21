package com.game.b1ingservice.payload.amb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResetPasswordReq {
    private String password;
    private String signature;
}
