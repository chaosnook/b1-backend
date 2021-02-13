package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.deposithistory.DepositHistorySearchResponse;
import com.game.b1ingservice.payload.thieve.ThieveResponse;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.postgres.entity.Thieve;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface DepositHistoryService {
    Page<DepositHistorySearchResponse> findByCriteria(Specification<DepositHistory> specification, Pageable pageable);
}
