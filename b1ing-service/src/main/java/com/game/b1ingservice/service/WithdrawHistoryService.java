package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.deposithistory.DepositHisUserReq;
import com.game.b1ingservice.payload.deposithistory.DepositHisUserRes;
import com.game.b1ingservice.payload.withdraw.WithdrawHisUserReq;
import com.game.b1ingservice.payload.withdraw.WithdrawHisUserRes;
import com.game.b1ingservice.postgres.entity.WithdrawHistory;

import java.util.List;

public interface WithdrawHistoryService {

    WithdrawHistory saveHistory(WithdrawHistory withdrawHistory);

    List<WithdrawHisUserRes> searchByUser(WithdrawHisUserReq withDrawRequest, String username);
}
