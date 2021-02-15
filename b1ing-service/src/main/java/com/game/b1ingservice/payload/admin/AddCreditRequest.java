package com.game.b1ingservice.payload.admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddCreditRequest {

    private String username;
    private BigDecimal credit;
}
