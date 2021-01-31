package com.game.b1ingservice.payload.bankdeposit;

import lombok.Data;

@Data
public class BankDepositAllRequest {
    String bankCodeFrom;
    Integer bankGroupFrom;
    String bankCodeTo;
    Integer bankGroupTo;
    String prefix;
}
