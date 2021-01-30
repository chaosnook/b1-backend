package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebUserRepository extends JpaRepository<WebUser, Long>, JpaSpecificationExecutor<WebUser> {

    boolean existsByTel(String tel);

    Optional<WebUser> findFirstByUsernameAndAgent_Prefix(String username, String prefix);
}
