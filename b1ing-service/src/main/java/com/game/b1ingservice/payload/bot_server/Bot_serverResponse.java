package com.game.b1ingservice.payload.bot_server;

import lombok.Data;

import java.time.Instant;

@Data
public class Bot_serverResponse {
    private Long id;
    private String botIp;
    private Instant createdDate;
    private Instant updatedDate;
    private String createdBy;
    private String updatedBy;
}
