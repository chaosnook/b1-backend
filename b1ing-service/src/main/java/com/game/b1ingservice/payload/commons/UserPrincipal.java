package com.game.b1ingservice.payload.commons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPrincipal implements Principal {
    private String userId;
    private String nameUser;


    @Override
    public String getName() {
        return userId;
    }
}
