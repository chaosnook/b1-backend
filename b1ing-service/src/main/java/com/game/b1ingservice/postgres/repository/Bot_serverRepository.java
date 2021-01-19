package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Bot_server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface Bot_serverRepository extends JpaRepository<Bot_server, Long>, JpaSpecificationExecutor<Bot_server> {
}
