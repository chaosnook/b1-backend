package com.game.b1ingservice.payload.admin;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProfitReportResponse {
    private List<Integer> labels = new ArrayList<>();
    private List<Integer> data = new ArrayList<>();
}
