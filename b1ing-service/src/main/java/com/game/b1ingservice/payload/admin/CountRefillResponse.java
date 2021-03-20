package com.game.b1ingservice.payload.admin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CountRefillResponse {
    private String username;
    private Integer countDeposit;
    private BigDecimal allDeposit;
    private Integer countWithdraw;
    private BigDecimal allWithdraw;

}
