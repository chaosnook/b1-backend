package com.game.b1ingservice.payload.withdrawhistory;

import com.game.b1ingservice.commons.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class WithdrawBlockStatusReq {

    private Long withdrawId;
    private Constants.WITHDRAW_STATUS status;
    private String reason;
}
