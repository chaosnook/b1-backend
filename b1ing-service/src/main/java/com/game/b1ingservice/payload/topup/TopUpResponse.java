package com.game.b1ingservice.payload.topup;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class TopUpResponse {
    private List<String> labels = new ArrayList<>();
    private List<String> data = new ArrayList<>();
}
