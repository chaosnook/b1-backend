package com.game.b1ingservice.payload.bankdeposit;

import com.game.b1ingservice.specification.commons.SearchPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class BankDepositRequest extends SearchPageable {
    String username;
    String bankCode;
    Integer bankGroup;
    Integer bankOrder;
    String prefix;
    Long bankId;
}
