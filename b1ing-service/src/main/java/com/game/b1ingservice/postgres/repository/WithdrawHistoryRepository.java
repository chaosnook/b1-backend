package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.WithdrawHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Repository
public interface WithdrawHistoryRepository extends JpaRepository<WithdrawHistory, Long>, JpaSpecificationExecutor<WithdrawHistory> {

    List<WithdrawHistory> findAllByUser_usernameAndCreatedDateBetweenAndStatusInOrderByCreatedDateDesc(String username, Instant startDate, Instant endDate, List<String> status);

    List<WithdrawHistory> findByUser_IdAndUser_Agent_PrefixOrderByCreatedDateDesc(Long id, String prefix);

    List<WithdrawHistory> findAllByUser_AgentAndCreatedDateBetweenAndMistakeTypeInOrderByCreatedDateDesc(Agent agent, Instant instantStart, Instant instantEnd, List<String> types);

    List<WithdrawHistory> findByIdAndAmount(Long id, BigDecimal amount);

    WithdrawHistory findFirstById(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE withdraw_history SET status = ? WHERE id = ? AND amount = ?", nativeQuery = true)
    int updateStatus(String status, Long id, BigDecimal amount);

    @Transactional
    @Modifying
    @Query(value = "UPDATE withdraw_history SET status = ?, reason = ?, isAuto = ? WHERE id = ? AND amount = ?", nativeQuery = true)
    int updateInfoWithdrawManual(String status, String reason, Long id, boolean isAuto, BigDecimal amount);
}
