package com.game.b1ingservice.postgres.jdbc.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SummaryRegisterUser implements Serializable {
    private Integer hourOfDay;
    private Integer dayOfMonth;
    private Integer monthOfYear;
    private Integer count;
    private Long agentId;
}
