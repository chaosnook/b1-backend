package com.game.b1ingservice.payload.withdrawhistory;

import com.game.b1ingservice.specification.commons.SearchPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class WithdrawHistorySearchRequest extends SearchPageable {

    private String createdDateFrom;
    private String createdDateTo;
    private String username;
    private String status;
    private String isAuto;

    private Boolean isMainPage;

    private Long agentId;
}
