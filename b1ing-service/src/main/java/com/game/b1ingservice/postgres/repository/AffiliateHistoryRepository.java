package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.AffiliateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AffiliateHistoryRepository extends JpaRepository<AffiliateHistory, Long>, JpaSpecificationExecutor<AffiliateHistory> {

}
