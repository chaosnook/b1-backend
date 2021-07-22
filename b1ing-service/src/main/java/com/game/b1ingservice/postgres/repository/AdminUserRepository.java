package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.AdminUser;
import com.game.b1ingservice.postgres.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long>, JpaSpecificationExecutor<AdminUser> {

    Optional<AdminUser> findByUsernameAndAgentAndActive(String username, Agent agent, int active);

    Optional<AdminUser> findByUsernameAndAgent_IdAndActive(String username, Long agentId, int active);

    boolean existsByUsernameAndAgent_Prefix(String username, String prefix);

    @Query("select o from AdminUser o where o.prefix = :prefix and o.role.roleCode <> 'XSUPERADMIN' order by -o.id")
    List<AdminUser> findByPrefix(String prefix);

    boolean existsByIdAndLastLoginToken(Long id, String token);

    boolean existsByIdAndAgent_Prefix(Long username, String prefix);

    @Transactional
    @Modifying
    @Query(value = "UPDATE admins SET mistake_limit = mistake_limit + 1 WHERE id = ? ", nativeQuery = true)
    int addMistake(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE admins SET last_login_token = ? WHERE username = ? and agent_id = ?", nativeQuery = true)
    int updateLastToken(String token, String username, Long agentId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE admins SET mistake_limit = 0", nativeQuery = true)
    void clearMistakeLimit();

}
