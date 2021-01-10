package com.game.b1ingservice.payload.commons;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorObj implements Serializable {
    private String code;
    private String description;
}
