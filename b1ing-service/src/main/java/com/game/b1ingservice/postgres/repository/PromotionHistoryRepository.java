package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.PromotionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionHistoryRepository extends JpaRepository<PromotionHistory, Long>, JpaSpecificationExecutor<PromotionHistory> {
}
