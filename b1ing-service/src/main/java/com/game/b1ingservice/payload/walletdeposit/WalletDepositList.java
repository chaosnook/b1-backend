package com.game.b1ingservice.payload.walletdeposit;

import lombok.Data;

@Data
public class WalletDepositList {
    private Long id;
    private String name;
    private Integer bankGroup;
    private String phoneNumber;
    private String prefix;
}
