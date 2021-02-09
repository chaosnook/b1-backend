package com.game.b1ingservice.payload.webuser;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WebUserHistoryResponse {
    private List<Integer> labels = new ArrayList<>();
    private List<Integer> data = new ArrayList<>();

}
