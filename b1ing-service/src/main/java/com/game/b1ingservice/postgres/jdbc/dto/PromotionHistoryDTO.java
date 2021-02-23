package com.game.b1ingservice.postgres.jdbc.dto;

import lombok.Data;

@Data
public class PromotionHistoryDTO {

    private Long id;
    private int topUp;
    private int bonus;
    private int turnOver;
}
