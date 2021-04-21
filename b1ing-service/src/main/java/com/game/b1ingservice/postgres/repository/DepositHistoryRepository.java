package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.postgres.entity.PromotionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepositHistoryRepository extends JpaRepository<DepositHistory, Long>, JpaSpecificationExecutor<DepositHistory> {
    boolean existsByTransactionId(String transactionId);

    List<DepositHistory> findAllByUser_usernameAndCreatedDateBetweenAndStatusOrderByCreatedDateDesc(String username, Instant startDate, Instant endDate, String status);

    List<DepositHistory> findTop10ByUser_IdOrderByCreatedDateDesc(Long id);

    List<DepositHistory> findAllByUser_AgentAndCreatedDateBetweenAndMistakeTypeInOrderByCreatedDateDesc(Agent agent, Instant startDate, Instant endDate, List<String> types);

    Boolean existsByUser_Id(Long userId);
}
