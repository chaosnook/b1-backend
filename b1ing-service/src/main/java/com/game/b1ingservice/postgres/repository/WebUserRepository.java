package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.WebUser;
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

    boolean existsByTelAndAgent_Id(String tel, Long agentId);

    boolean existsByTelAndAgent_Prefix(String tel, String prefix);

    Optional<WebUser> findFirstByUsernameAndAgent_Id(String username, Long agentId);

    Optional<WebUser> findFirstByUsernameAndAgent_Prefix(String username, String prefix);

    Optional<WebUser> findByUsernameOrTelAndAgent_Id(String affiliateUsername, String affiliateTel, Long agentId);

    Optional<WebUser> findByUsernameAndAgent(String username, Agent agent);

    Optional<WebUser> findByIdAndAgent_Id(Long id, Long agentId);

    Optional<WebUser> findByIdNotAndUsernameAndAgent_Id(Long id, String username, Long agentId);

    @Query(value = "select u.id, username, username_amb as usernameAmb, deposit_ref as depositRef, w.updated_date FROM users u inner join wallet w on u.id = w.user_id where agent_id = ? and u.delete_flag = 0 and deposit_ref is not null and w.updated_date >= now() - INTERVAL '3 DAYS'", nativeQuery = true)
    List<Map> getAllUser(Long agentId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET deposit_ref = ? WHERE id = ? ", nativeQuery = true)
    void updateDepositRef(String ref, Long userId);

    boolean existsByAccountNumberAndAgent_Id(String accountNumber, Long agentId);

    boolean existsByAccountNumberAndAgent_Prefix(String accountNumber, String prefix);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET withdraw_limit = withdraw_limit + 1 WHERE id = ? ", nativeQuery = true)
    int addCountWithdraw(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET withdraw_limit = 0", nativeQuery = true)
    void clearCountWithdraw();
}

