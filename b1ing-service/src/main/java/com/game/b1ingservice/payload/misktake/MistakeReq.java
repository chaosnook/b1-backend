package com.game.b1ingservice.payload.misktake;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MistakeReq {
    private String type;
    private String username;
    private BigDecimal credit;
    private String reason;
    private Integer turnOver;
}
