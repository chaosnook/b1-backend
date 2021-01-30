package com.game.b1ingservice.payload.walletdeposit;

import lombok.Data;

@Data
public class WalletDepositResponse {
    private Long id;
    private String username;
    private Integer bankGroup;
}
