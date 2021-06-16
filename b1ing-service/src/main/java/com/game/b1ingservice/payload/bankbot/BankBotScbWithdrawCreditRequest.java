package com.game.b1ingservice.payload.bankbot;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.game.b1ingservice.serializer.MoneyDeserializer;
import com.game.b1ingservice.serializer.MoneySerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BankBotScbWithdrawCreditRequest implements Serializable {
    private String accountTo;
    private String  bankCode;
    @JsonSerialize(using = MoneySerializer.class)
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal amount;
}
