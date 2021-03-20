package com.game.b1ingservice.payload.amb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WinLoseDataList {

    @JsonProperty("FOOTBALL")
    private WinLoseData FOOTBALL;
    @JsonProperty("STEP")
    private WinLoseData STEP;
    @JsonProperty("PARLAY")
    private WinLoseData PARLAY;
    @JsonProperty("GAME")
    private WinLoseData GAME;
    @JsonProperty("CASINO")
    private WinLoseData CASINO;
    @JsonProperty("LOTTO")
    private WinLoseData LOTTO;
    @JsonProperty("M2")
    private WinLoseData M2;
    @JsonProperty("MULTI_PLAYER")
    private WinLoseData MULTI_PLAYER;
}
