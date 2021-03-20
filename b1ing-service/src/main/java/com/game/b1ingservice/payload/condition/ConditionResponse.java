package com.game.b1ingservice.payload.condition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.game.b1ingservice.serializer.MoneyDeserializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConditionResponse {
    private Long id;
    private BigDecimal minTopup;
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal maxTopup;
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal bonus;
}
