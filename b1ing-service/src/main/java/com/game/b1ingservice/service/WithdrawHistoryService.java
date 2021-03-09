package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.withdraw.WithdrawHisUserReq;
import com.game.b1ingservice.payload.withdraw.WithdrawHisUserRes;
import com.game.b1ingservice.payload.withdrawhistory.WithdrawHistoryByUserIdResp;
import com.game.b1ingservice.payload.withdrawhistory.WithdrawHistoryUpdateStatusReq;
import com.game.b1ingservice.payload.withdrawhistory.WithdrawListHistorySearchResponse;
import com.game.b1ingservice.payload.withdrawhistory.WithdrawSummaryHistorySearchResponse;
import com.game.b1ingservice.postgres.entity.WithdrawHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public interface WithdrawHistoryService {

    Page<WithdrawListHistorySearchResponse> findByCriteria(Specification<WithdrawHistory> specification, Pageable pageable, String type);

    Page<WithdrawSummaryHistorySearchResponse> findSummaryByCriteria(Specification<WithdrawHistory> specification, Pageable pageable, String type);

    WithdrawHistory saveHistory(WithdrawHistory withdrawHistory);

    List<WithdrawHisUserRes> searchByUser(WithdrawHisUserReq withDrawRequest, String username);

    List<WithdrawHistoryByUserIdResp> findListByUserId(Long userId);

    void updateStatus(WithdrawHistoryUpdateStatusReq req);
}
