package com.game.b1ingservice.payload.misktake;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MistakeSearchSummaryRes {
    private BigDecimal noSlip;
    private BigDecimal cutCredit;
    private BigDecimal addCredit;
}
