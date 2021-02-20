package com.game.b1ingservice.payload.amb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WithdrawRes {
    private String before;
    private String amount;
    private String after;
    private String ref;
}
