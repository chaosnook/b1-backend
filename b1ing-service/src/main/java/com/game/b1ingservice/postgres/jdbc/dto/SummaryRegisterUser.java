package com.game.b1ingservice.postgres.jdbc.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SummaryRegisterUser implements Serializable {
    private Integer labels;
    private Integer data;
//    private Long agentId;
}
