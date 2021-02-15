package com.game.b1ingservice.payload.Url;

import lombok.Data;

@Data
public class UrlRequest {
    private Long id;
    private String parameter;
    private String value;
    private String type;
}
