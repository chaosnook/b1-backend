package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.PromotionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface PromotionHistoryRepository extends JpaRepository<PromotionHistory, Long>, JpaSpecificationExecutor<PromotionHistory> {
    Boolean existsByUser_IdAndPromotion_Id(Long userId, Long promotionId);

    Integer countByUser_IdAndPromotion_IdAndCreatedDateBetween(Long userId, Long promotionId, Instant start, Instant end);
}
