package com.game.b1ingservice.payload.amb;

import lombok.Data;

@Data
public class GameStatusRes {
    private Boolean active;
    private Boolean lock;
    private Boolean suspend;
}
