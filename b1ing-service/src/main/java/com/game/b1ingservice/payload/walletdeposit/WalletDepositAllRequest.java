package com.game.b1ingservice.payload.walletdeposit;

import lombok.Data;

@Data
public class WalletDepositAllRequest {
    Integer bankGroupFrom;
    Integer bankGroupTo;
    String prefix;
}