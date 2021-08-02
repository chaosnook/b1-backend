package com.game.b1ingservice.payload.bankbot;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.game.b1ingservice.serializer.MoneyDeserializer;
import com.game.b1ingservice.serializer.MoneySerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class BankBotAddCreditRequest implements Serializable {
    private String botType;
    private String transactionId;
    private String botIp;
    private String bankName;

    private String accountNo;
    @JsonSerialize(using = MoneySerializer.class)
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal amount;
    private Instant transactionDate;
    private String type;

    private String firstName;
    private String lastName;

    private String remark;


}
