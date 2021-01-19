package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.AdminUser;
import com.game.b1ingservice.postgres.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByRoleCode(String roleCode);
    boolean existsByRoleCode(String roleCode);
    Optional<Role> findByIdAndDeleteFlag(Long id, int deleteFlag);
}
