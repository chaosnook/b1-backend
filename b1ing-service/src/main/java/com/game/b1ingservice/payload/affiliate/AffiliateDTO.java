package com.game.b1ingservice.payload.affiliate;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AffiliateDTO {
    private Long id;
    private String typeBonus;
    private BigDecimal maxBonus;
    private BigDecimal maxWallet;
    private BigDecimal maxWithdraw;
    private String img;
    private boolean active;
    private boolean isForever;

    private int version;

    List<AffConditionRequest> conditions;
}
