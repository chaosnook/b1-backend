package com.game.b1ingservice.payload.bankbot;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.game.b1ingservice.serializer.MoneyDeserializer;
import com.game.b1ingservice.serializer.MoneySerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BankBotScbWithdrawCreditResponse implements Serializable {

    private Boolean status;
    @JsonSerialize(using = MoneySerializer.class)
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal remainingBalance;
    private String transactionId;
    private String transactionDateTime;
    private String qrString;
    private String messege;
}
