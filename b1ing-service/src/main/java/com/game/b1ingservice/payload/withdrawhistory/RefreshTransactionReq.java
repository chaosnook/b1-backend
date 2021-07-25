package com.game.b1ingservice.payload.withdrawhistory;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class RefreshTransactionReq {
    private Long withdrawId;
}
