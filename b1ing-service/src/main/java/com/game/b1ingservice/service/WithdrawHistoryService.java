package com.game.b1ingservice.service;

import com.game.b1ingservice.postgres.entity.WithdrawHistory;

public interface WithdrawHistoryService {

    WithdrawHistory saveHistory(WithdrawHistory withdrawHistory);
}
