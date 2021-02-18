package com.game.b1ingservice.payload.amb;

import lombok.Data;

@Data
public class WithdrawRes {
    private String before;
    private String amount;
    private String after;
    private String ref;
}
