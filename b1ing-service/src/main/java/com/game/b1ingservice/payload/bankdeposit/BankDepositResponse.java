package com.game.b1ingservice.payload.bankdeposit;

import lombok.Data;

@Data
public class BankDepositResponse {
    private Long id;
    private String username;
    private Long bankId;
    private String bankCode;
    private String bankName;
    private Integer bankGroup;
    private Integer bankOrder;
}
