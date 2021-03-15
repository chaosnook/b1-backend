package com.game.b1ingservice.payload.amb;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WinLoseData {
    private String game;
    private BigDecimal amount;
    private BigDecimal validAmount;
    private BigDecimal wlTurnAmount;
    private BigDecimal outstanding;
}
