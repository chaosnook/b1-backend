package com.game.b1ingservice.payload.deposithistory;

import com.game.b1ingservice.commons.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class DepositBlockStatusReq {

    private Long depositId;
    private Constants.DEPOSIT_STATUS status;
    private String reason;
}
