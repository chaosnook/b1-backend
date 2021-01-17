package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long>, JpaSpecificationExecutor<AdminUser> {
    Optional<AdminUser> findByUsername(String username);
    Optional<AdminUser> findByUsernameAndPrefixAndActive(String username,String prefix,int active);
    boolean existsByUsername(String username);
    Optional<AdminUser> findByTel(String tel);
    @Query("select o from AdminUser o where o.prefix = :prefix and o.role.roleCode <> 'XSUPERADMIN' ")
    List<AdminUser> findByPrefix(String prefix);

}
