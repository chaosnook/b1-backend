package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface DepositHistoryRepository extends JpaRepository<DepositHistory, Long>, JpaSpecificationExecutor<DepositHistory> {
    boolean existsByTransactionId(String transactionId);

    List<DepositHistory> findAllByUser_usernameAndCreatedDateBetweenAndStatusInOrderByCreatedDateDesc(String username, Instant startDate, Instant endDate, List<String> status);

    List<DepositHistory> findTop10ByUser_IdOrderByCreatedDateDesc(Long id);

    List<DepositHistory> findAllByUser_AgentAndCreatedDateBetweenAndMistakeTypeInOrderByCreatedDateDesc(Agent agent, Instant startDate, Instant endDate, List<String> types);

    Boolean existsByUser_Id(Long userId);

    DepositHistory findFirstById(Long withdrawId);

    DepositHistory findFirstByIdAndStatus(Long withdrawId, String status);

    Page<DepositHistory> findByUser_Agent_PrefixAndStatusOrderByCreatedDateDesc(String prefix , String status, Pageable pageable);
}
