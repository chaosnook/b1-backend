package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.DepositHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface DepositHistoryRepository extends JpaRepository<DepositHistory, Long>, JpaSpecificationExecutor<DepositHistory> {
    boolean existsByTransactionId(String transactionId);

    List<DepositHistory> findAllByUser_usernameAndCreatedDateBetweenOrderByCreatedDateDesc(String username, Instant startDate, Instant endDate);

    List<DepositHistory> findTop10ByUser_IdOrderByCreatedDateDesc(Long id);

}
