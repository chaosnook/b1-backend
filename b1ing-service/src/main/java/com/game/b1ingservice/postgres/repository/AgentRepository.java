package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long>, JpaSpecificationExecutor<Agent> {
    Optional<Agent> findByPrefix(String prefix);
}
