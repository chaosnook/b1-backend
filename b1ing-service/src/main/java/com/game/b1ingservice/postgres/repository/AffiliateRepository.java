package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Affiliate;
import com.game.b1ingservice.postgres.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AffiliateRepository extends JpaRepository<Affiliate, Long>, JpaSpecificationExecutor<Affiliate> {

    Affiliate findFirstByAgent(Agent agent);

    Affiliate findFirstByAgent_Prefix(String prefix);
}
