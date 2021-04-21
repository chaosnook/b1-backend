package com.game.b1ingservice.payload.promotion;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.serializer.MoneyDeserializer;
import com.game.b1ingservice.serializer.MoneySerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PromotionEffectiveResponse {

    @JsonSerialize(using = MoneySerializer.class)
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal bonus;
    @JsonSerialize(using = MoneySerializer.class)
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal turnOver;

}
