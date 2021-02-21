package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.Config;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long>, JpaSpecificationExecutor<Config> {

    Optional<Config> findFirstByParameterAndAgent(String parameter, Agent agent);

    List<Config> findAllByTypeAndAgent(String type, Agent agent);
}
