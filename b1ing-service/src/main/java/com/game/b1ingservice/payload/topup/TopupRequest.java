package com.game.b1ingservice.payload.topup;

import lombok.Data;

@Data
public class TopupRequest {
    private String listDateFrom;
    private String listDateTo;
}
