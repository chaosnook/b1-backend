package com.game.b1ingservice.payload.userinfo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserWalletResponse {
    private BigDecimal point;
    private BigDecimal credit;
}
