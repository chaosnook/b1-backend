package com.game.b1ingservice.payload.condition;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConditionListRequest {
    private List<Integer> condition = new ArrayList<>();
}
