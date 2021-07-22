package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long>, JpaSpecificationExecutor<Promotion> {

    Optional<Promotion> findById(Long id);

    List<Promotion> findAllByAgent_IdAndActive(Long agentId , boolean active);

    @Query("select o from Promotion o where o.active = true and :amount between o.minTopup and o.maxTopup and o.agent.id = :agentId")
    List<Promotion> findByMaxTopupAndMinTopupAndAgent(BigDecimal amount,Long agentId);

    List<Promotion> findAllByAgent_IdOrderByIdDesc(Long agentId);
}
