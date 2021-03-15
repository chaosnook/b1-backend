package com.game.b1ingservice.payload.amb;

import lombok.Data;

@Data
public class WinLoseResponse {
    private String username;
    private String ref;
    private WinLoseDataList data;
}
