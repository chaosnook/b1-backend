package com.game.b1ingservice.postgres.jdbc.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PointHistoryDTO {

    private Long id;
    private BigDecimal amount;
    private BigDecimal beforeAmount;
    private BigDecimal afterAmount;
    private String type;
    private String status;
    private String reason;

}
