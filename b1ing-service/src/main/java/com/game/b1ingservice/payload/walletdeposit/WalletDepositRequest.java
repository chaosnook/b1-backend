package com.game.b1ingservice.payload.walletdeposit;

import com.game.b1ingservice.specification.commons.SearchPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class WalletDepositRequest extends SearchPageable {
    String username;
    Integer bankGroup;
    String prefix;
}
