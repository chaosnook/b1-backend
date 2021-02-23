package com.game.b1ingservice.payload.deposithistory;

import com.game.b1ingservice.specification.commons.SearchPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class DepositHistoryTop20Req  extends SearchPageable {

    private String username;
}
