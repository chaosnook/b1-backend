package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.postgres.entity.WithdrawHistory;
import com.game.b1ingservice.postgres.repository.WithdrawHistoryRepository;
import com.game.b1ingservice.service.WithdrawHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WithdrawHistoryServiceImpl implements WithdrawHistoryService {

    @Autowired
    private WithdrawHistoryRepository withdrawHistoryRepository;

    @Override
    public WithdrawHistory saveHistory(WithdrawHistory withdrawHistory) {
        return withdrawHistoryRepository.save(withdrawHistory);
    }
}
