package com.game.b1ingservice.payload.deposithistory;

import lombok.Data;

@Data
public class DepositHistoryByUserIdResp {

    private String bankName;
    private String bankCode;
    private String amount;
    private String bonus;
    private String addCredit;
    private String beforeAmount;
    private String afterAmount;
    private String createdDate;
    private String reason;
}
