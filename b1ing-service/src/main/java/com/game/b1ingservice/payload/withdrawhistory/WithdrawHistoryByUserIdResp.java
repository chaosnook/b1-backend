package com.game.b1ingservice.payload.withdrawhistory;

import lombok.Data;

@Data
public class WithdrawHistoryByUserIdResp {

    private String bankName;
    private String bankCode;
    private String type;
    private String amount;
    private String status;
    private String createdDate;
    private String reason;
    private String qrCode;
}
