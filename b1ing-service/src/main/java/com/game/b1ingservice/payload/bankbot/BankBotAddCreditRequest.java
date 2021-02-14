package com.game.b1ingservice.payload.bankbot;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class BankBotAddCreditRequest implements Serializable {
    private String botType;
    private String transactionId;
    private Long bankId;
    private String bankCode;
    private String bankAccountNo;

    private String accountNo;
    private BigDecimal amount;
    private Instant transactionDate;
    private String type;

    private String firstName;
    private String lastName;


}
