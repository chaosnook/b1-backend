package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.AffiliateHistory;
import com.game.b1ingservice.postgres.entity.AffiliateUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AffiliateUserRepository extends JpaRepository<AffiliateUser, Long>, JpaSpecificationExecutor<AffiliateUser> {

    Optional<AffiliateUser> findFirstByAffiliateAndUser_Id(String affiliate, Long id);

    List<AffiliateUser> findAllByUser_Id(Long id);


    @Transactional
    @Modifying
    @Query(value = "UPDATE affiliate_user SET delete_flag = 1 WHERE user_id = ? ", nativeQuery = true)
    void removeOleAffiliate(Long id);
}
