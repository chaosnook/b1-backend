package com.game.b1ingservice.payload.deposithistory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DepositHisUserReq {
    private String startDate;
    private String endDate;
}
