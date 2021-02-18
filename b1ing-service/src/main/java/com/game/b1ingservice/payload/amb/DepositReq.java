package com.game.b1ingservice.payload.amb;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DepositReq {
    private String amount;
    private String signature;

}
