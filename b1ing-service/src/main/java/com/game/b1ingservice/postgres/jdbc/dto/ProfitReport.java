package com.game.b1ingservice.postgres.jdbc.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProfitReport {
    private Integer labels;
    private BigDecimal data;
}
