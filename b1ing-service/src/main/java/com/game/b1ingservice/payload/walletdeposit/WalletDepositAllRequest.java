package com.game.b1ingservice.payload.walletdeposit;

import lombok.Data;

@Data
public class WalletDepositAllRequest {
    Long trueWalletIdFrom;
    Long trueWalletIdTo;
    Long agentId;
}
