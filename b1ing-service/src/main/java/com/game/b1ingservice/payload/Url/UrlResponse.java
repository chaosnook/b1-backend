package com.game.b1ingservice.payload.Url;

import lombok.Data;

@Data
public class UrlResponse {
    private Long agentId;
    private String parameter;
    private String value;
    private String type;
}
