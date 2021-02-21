package com.game.b1ingservice.payload.amb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AmbResponse<T> {
    private Integer code;
    private T result;
}
