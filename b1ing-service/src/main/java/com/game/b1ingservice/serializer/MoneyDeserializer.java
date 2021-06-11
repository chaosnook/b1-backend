package com.game.b1ingservice.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class MoneyDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext context) throws IOException, JsonProcessingException {
        BigDecimal result = BigDecimal.ZERO;
        if (p.getText() != null && !p.getText().equals("")) {
            result = new BigDecimal(p.getText());
        }
        return result;
    }
}