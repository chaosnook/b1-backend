package com.game.b1ingservice.payload.bank;

import lombok.Data;

@Data
public class BankAllRequest {
    Long bankIdFrom;
    Long bankIdTo;
}
