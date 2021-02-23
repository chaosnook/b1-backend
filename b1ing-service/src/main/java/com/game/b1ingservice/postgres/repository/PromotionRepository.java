package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long>, JpaSpecificationExecutor<Promotion> {

    Optional<Promotion> findById(Long id);

    List<Promotion> findAllByAgent_PrefixAndActive(String prefix , boolean active);
}
