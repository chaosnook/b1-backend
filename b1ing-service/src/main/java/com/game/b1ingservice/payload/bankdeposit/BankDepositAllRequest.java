package com.game.b1ingservice.payload.bankdeposit;

import lombok.Data;

@Data
public class BankDepositAllRequest {
    Long bankIdFrom;
    Long bankIdTo;
    String prefix;
}
