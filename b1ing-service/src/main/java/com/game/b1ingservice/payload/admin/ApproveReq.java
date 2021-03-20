package com.game.b1ingservice.payload.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApproveReq {

    private Long id;
    private BigDecimal amount;
}
