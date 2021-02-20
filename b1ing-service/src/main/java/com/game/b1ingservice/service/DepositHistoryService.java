package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.deposithistory.DepositHisUserReq;
import com.game.b1ingservice.payload.deposithistory.DepositHisUserRes;
import com.game.b1ingservice.payload.deposithistory.DepositListHistorySearchResponse;
import com.game.b1ingservice.payload.deposithistory.DepositSummaryHistorySearchResponse;
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
}
