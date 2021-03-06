package com.game.b1ingservice.payload.promotion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.game.b1ingservice.serializer.MoneyDeserializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PromotionUpdate {
    private Long id;
    private String name;
    private String type;
    private String typeBonus ;
    private String typePromotion ;
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal maxBonus;
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal minTopup;
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal maxTopup;
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal maxReceiveBonus;
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal turnOver;
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal maxWithdraw;
    private boolean active;
    private String urlImage;
}
