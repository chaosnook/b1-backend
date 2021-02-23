package com.game.b1ingservice.payload.webuser;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetUserInfoResponse {

    private String name;
    private String tel;
    private String bankName;
    private String bankAccount;
    private BigDecimal credit;
    private BigDecimal turnOver;
}
