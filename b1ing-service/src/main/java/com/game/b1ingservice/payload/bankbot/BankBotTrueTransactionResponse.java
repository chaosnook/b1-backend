package com.game.b1ingservice.payload.bankbot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BankBotTrueTransactionResponse implements Serializable {
    private String tranDate;
    private String tranID;
    private Double amount;
    private String mobile;
}
