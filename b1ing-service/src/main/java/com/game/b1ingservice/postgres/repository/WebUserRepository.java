package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.payload.webuser.WebUserSearchRequest;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.entity.WebUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface WebUserRepository extends JpaRepository<WebUser, Long>, JpaSpecificationExecutor<WebUser> {

    boolean existsByTel(String tel);

    Optional<WebUser> findFirstByUsernameAndAgent_Prefix(String username, String prefix);

    Optional<WebUser> findByUsernameOrTel(String affiliateUsername, String affiliateTel);

    Optional<WebUser> findByTelAndAgent_Prefix(String tel, String prefix);

    Optional<WebUser> findByUsernameAndAgent(String username, Agent agent);

    Optional<WebUser> findByIdNotAndUsername(Long id, String username);

//    @Query(value = "select o from WebUser o where o.depositHistory.size>0")
//    List<WebUser> findDepositUsers();

    @Query(value = "select id, username, deposit_ref as depositRef FROM users  where agent_id = ? and delete_flag = 0 and deposit_ref is not null", nativeQuery = true)
    List<Map> getAllUser(Long agentId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET deposit_ref = ? WHERE id = ? ", nativeQuery = true)
    void updateDepositRef(String ref, Long userId);
}

