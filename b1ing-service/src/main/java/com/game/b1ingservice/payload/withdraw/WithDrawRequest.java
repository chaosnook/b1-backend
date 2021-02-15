package com.game.b1ingservice.payload.withdraw;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithDrawRequest {
    private BigDecimal creditWithDraw;
}
