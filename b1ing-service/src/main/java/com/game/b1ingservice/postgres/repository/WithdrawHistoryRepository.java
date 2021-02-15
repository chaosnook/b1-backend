package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.WithdrawHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawHistoryRepository extends JpaRepository<WithdrawHistory, Long>, JpaSpecificationExecutor<WithdrawHistory> {
}
