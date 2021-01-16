package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long>, JpaSpecificationExecutor<AdminUser> {
    Optional<AdminUser> findByUsername(String username);
    Optional<AdminUser> findByUsernameAndPrefix(String username,String prefix);
    boolean existsByUsername(String username);
    Optional<AdminUser> findByTel(String tel);
}
