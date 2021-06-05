package com.game.b1ingservice.payload.withdrawhistory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class WithdrawHistoryTopAll20Resp {

    private String username;
    private String bankName;
    private String bankCode;
    private String type;
    private String amount;
    private String status;
    private String createdDate;
    private String reason;
    private String qrCode;
}
