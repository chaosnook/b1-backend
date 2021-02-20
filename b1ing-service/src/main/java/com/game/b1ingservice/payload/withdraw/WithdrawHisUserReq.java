package com.game.b1ingservice.payload.withdraw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class WithdrawHisUserReq {
    private String startDate;
    private String endDate;
}
