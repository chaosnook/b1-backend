package com.game.b1ingservice.postgres.jdbc.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CheckPromotion7Days {
    private Date date;
    private Integer count;
}
