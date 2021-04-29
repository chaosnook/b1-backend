package com.game.b1ingservice.payload.amb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinLoseReq implements Serializable {
    private Long id;
    private String username;

    @JsonProperty("usernameamb")
    private String usernameAmb;

    @JsonProperty("depositref")
    private String depositRef;
}
