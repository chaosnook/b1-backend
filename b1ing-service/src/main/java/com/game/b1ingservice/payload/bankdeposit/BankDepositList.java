package com.game.b1ingservice.payload.bankdeposit;

import lombok.Data;

@Data
public class BankDepositList {
    private Long id;
    private String bankCode;
    private String bankName;
    private Integer bankGroup;
    private Integer bankOrder;

}
