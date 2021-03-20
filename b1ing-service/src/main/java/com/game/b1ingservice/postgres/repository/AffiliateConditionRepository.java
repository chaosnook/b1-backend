package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.AffiliateCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface AffiliateConditionRepository extends JpaRepository<AffiliateCondition, Long>, JpaSpecificationExecutor<AffiliateCondition> {

    @Modifying
    void deleteAffiliateConditionByAffiliate_Id(Long id);
}
