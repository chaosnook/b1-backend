package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.withdraw.WithdrawHisUserReq;
import com.game.b1ingservice.payload.withdraw.WithdrawHisUserRes;
import com.game.b1ingservice.postgres.entity.WithdrawHistory;
import com.game.b1ingservice.postgres.repository.WithdrawHistoryRepository;
import com.game.b1ingservice.service.WithdrawHistoryService;
import com.game.b1ingservice.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WithdrawHistoryServiceImpl implements WithdrawHistoryService {

    @Autowired
    private WithdrawHistoryRepository withdrawHistoryRepository;

    @Override
    public WithdrawHistory saveHistory(WithdrawHistory withdrawHistory) {
        return withdrawHistoryRepository.save(withdrawHistory);
    }

    @Override
    public List<WithdrawHisUserRes> searchByUser(WithdrawHisUserReq withDrawRequest, String username) {
        List<WithdrawHistory> depositHistories = new ArrayList<>();
        if (withDrawRequest.getStartDate() != null && withDrawRequest.getPrevDate() != null) {
            depositHistories = withdrawHistoryRepository.findAllByUser_usernameAndCreatedDateBetweenOrderByCreatedDateDesc(username,
                    DateUtils.atStartOfDay(DateUtils.convertStartDate(withDrawRequest.getPrevDate())).toInstant(),
                    DateUtils.atEndOfDay(DateUtils.convertEndDate(withDrawRequest.getStartDate())).toInstant());
        } else if (withDrawRequest.getStartDate() != null) {
            depositHistories = withdrawHistoryRepository.findAllByUser_usernameAndCreatedDateBetweenOrderByCreatedDateDesc(username,
                    DateUtils.atStartOfDay(DateUtils.convertStartDate(withDrawRequest.getStartDate())).toInstant(),
                    DateUtils.atEndOfDay(DateUtils.convertEndDate(withDrawRequest.getStartDate())).toInstant());
        }
        return depositHistories.stream().map(converterUser).collect(Collectors.toList());
    }

    Function<WithdrawHistory, WithdrawHisUserRes> converterUser = withdrawHistory -> {
        WithdrawHisUserRes witHis = new WithdrawHisUserRes();
        witHis.setReason(withdrawHistory.getReason());
        witHis.setCreatedDate(withdrawHistory.getCreatedDate());
        witHis.setStatus(withdrawHistory.getStatus());
        witHis.setValue(withdrawHistory.getAmount());
        return witHis;
    };
}
