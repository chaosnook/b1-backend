package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.postgres.entity.WithdrawHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface WithdrawHistoryRepository extends JpaRepository<WithdrawHistory, Long>, JpaSpecificationExecutor<WithdrawHistory> {

    List<WithdrawHistory> findAllByUser_usernameAndCreatedDateBetweenOrderByCreatedDateDesc(String username, Instant startDate, Instant endDate);

    List<WithdrawHistory> findTop10ByUser_IdOrderByCreatedDateDesc(Long id);

}
