package com.game.b1ingservice.payload.admin;

import lombok.Data;

@Data
public class CountRefillRequest {
    private String listDateFrom;
    private String listDateTo;
}
