package com.game.b1ingservice.payload.bankdeposit;

import lombok.Data;

@Data
public class UserBankDepositResponse {
    private String bankCode;
    private String bankName;
    private String bankAccountNo;
    private String bankAccountName;
}
