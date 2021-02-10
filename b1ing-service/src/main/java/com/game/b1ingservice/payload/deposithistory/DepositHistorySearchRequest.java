package com.game.b1ingservice.payload.deposithistory;

import com.game.b1ingservice.specification.commons.SearchPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class DepositHistorySearchRequest extends SearchPageable {

    private String createdDateFrom;
    private String createdDateTo;
    private String username;
    private BigDecimal credit;
}
