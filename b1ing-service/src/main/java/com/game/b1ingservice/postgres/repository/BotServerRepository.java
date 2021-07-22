package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.BotServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BotServerRepository extends JpaRepository<BotServer, Long>, JpaSpecificationExecutor<BotServer> {

    List<BotServer> findAllByAgent_Id(Long agent);

    Optional<BotServer> findByIdAndAgent_Id(Long id, Long agentId);

    BotServer findFirstByBotIpAndAgent_Id(String ip, Long agentId);
}
