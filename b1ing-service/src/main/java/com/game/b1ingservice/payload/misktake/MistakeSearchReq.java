package com.game.b1ingservice.payload.misktake;

import lombok.Data;

@Data
public class MistakeSearchReq {
    private String startDate;
    private String endDate;
    private String type;
}
