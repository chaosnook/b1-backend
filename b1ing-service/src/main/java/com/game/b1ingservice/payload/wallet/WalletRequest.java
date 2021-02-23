package com.game.b1ingservice.payload.wallet;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletRequest {
    private BigDecimal credit;
    private BigDecimal point;
}
