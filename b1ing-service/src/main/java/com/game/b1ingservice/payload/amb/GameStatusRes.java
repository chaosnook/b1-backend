package com.game.b1ingservice.payload.amb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameStatusRes {
    private Boolean active;
    private Boolean lock;
    private Boolean suspend;
}
