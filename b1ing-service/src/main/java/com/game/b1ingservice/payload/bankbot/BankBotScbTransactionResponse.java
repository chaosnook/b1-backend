package com.game.b1ingservice.payload.bankbot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BankBotScbTransactionResponse implements Serializable {
    private String txnDateTime;
    private String txnRemark;
    private Double txnAmount;
    private String accountNo;
}
