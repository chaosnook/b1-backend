package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.AdminUser;
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
    Optional<AdminUser> findByUsername(String username);
    Optional<AdminUser> findByUsernameAndPrefixAndActive(String username,String prefix,int active);
    boolean existsByUsername(String username);
    Optional<AdminUser> findByTel(String tel);
    @Query("select o from AdminUser o where o.prefix = :prefix and o.role.roleCode <> 'XSUPERADMIN' order by -o.id")
    List<AdminUser> findByPrefix(String prefix);

    @Transactional
    @Modifying
    @Query(value = "UPDATE admins SET mistake_limit = mistake_limit + 1 WHERE id = ? ", nativeQuery = true)
    int addMistake(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE admins SET last_login_token = ? WHERE username = ? ", nativeQuery = true)
    int updateLastToken(String token, String username);

    boolean existsByIdAndLastLoginToken(Long id, String token);

    @Transactional
    @Modifying
    @Query(value = "UPDATE admins SET mistake_limit = 0", nativeQuery = true)
    void clearMistakeLimit();

    boolean existsByIdAndAgent_Prefix(Long username, String prefix);
}
