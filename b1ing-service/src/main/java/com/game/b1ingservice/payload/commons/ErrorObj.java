package com.game.b1ingservice.payload.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorObj implements Serializable {
    private String code;
    private String description;
}
