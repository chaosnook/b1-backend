package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.deposithistory.*;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepositHistoryService {
    Page<DepositListHistorySearchResponse> findByCriteria(Specification<DepositHistory> specification, Pageable pageable, String type);

    Page<DepositSummaryHistorySearchResponse> findSummaryByCriteria(Specification<DepositHistory> specification, Pageable pageable, String type);

    List<DepositHisUserRes> searchByUser(DepositHisUserReq depositHisUserReq, String username);

    List<DepositHistoryTop20Resp> findListByUsername(String username, UserPrincipal principal);

    List<DepositHistoryByUserIdResp> findListByUserId(Long userId);

    ProfitAndLossResp findProfitAndLoss(ProfitAndLossRequest req, UserPrincipal principal);


    DepositResponse updateBlockStatus(DepositBlockStatusReq req, String username);

    DepositResponse updateNoteSureStatus(DepositNotSureStatusReq req, String username, String prefix);
}
