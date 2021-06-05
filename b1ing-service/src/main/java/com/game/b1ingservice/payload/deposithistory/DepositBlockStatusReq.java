package com.game.b1ingservice.payload.deposithistory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class DepositBlockStatusReq {

    private Long depositId;
    private String status;
    private String reason;
}
