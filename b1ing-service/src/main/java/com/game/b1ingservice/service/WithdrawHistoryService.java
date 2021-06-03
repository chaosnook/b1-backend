package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.withdraw.WithDrawResponse;
import com.game.b1ingservice.payload.withdraw.WithdrawHisUserReq;
import com.game.b1ingservice.payload.withdraw.WithdrawHisUserRes;
import com.game.b1ingservice.payload.withdrawhistory.*;
import com.game.b1ingservice.postgres.entity.WithdrawHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface WithdrawHistoryService {

    Page<WithdrawListHistorySearchResponse> findByCriteria(Specification<WithdrawHistory> specification, Pageable pageable, String type);

    Page<WithdrawSummaryHistorySearchResponse> findSummaryByCriteria(Specification<WithdrawHistory> specification, Pageable pageable, String type);

    WithdrawHistory saveHistory(WithdrawHistory withdrawHistory);

    List<WithdrawHisUserRes> searchByUser(WithdrawHisUserReq withDrawRequest, String username);

    List<WithdrawHistoryByUserIdResp> findListByUserId(Long userId, UserPrincipal principal);

    void updateStatus(WithdrawHistoryUpdateStatusReq req);

    WithDrawResponse updateBlockStatus(WithdrawBlockStatusReq req, String username);
}
