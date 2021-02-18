package com.game.b1ingservice.payload.amb;

import lombok.Data;

@Data
public class AmbResponse<T> {
    private int code;
    private T result;
}
