package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long>, JpaSpecificationExecutor<Bank> {
    Optional<Bank> findById(Long id);

    Optional<Bank> findByIdAndAgent_Id(Long id, Long agentId);

    List<Bank> findAllByAgent_Id(Long agentId);

    boolean existsByBankOrderAndAgent_Id(int bankGroup, Long agentId);

    boolean existsByBankGroupAndAgent_Id(int bankOrder, Long agentId);

    Optional<Bank> findFirstByBankTypeAndActiveAndAgent_IdOrderByBankGroupAscBankOrderAsc(String bankType, boolean isActive, Long agentId);

    List<Bank> findAllByActiveAndAgent_IdOrderByBankGroupAscBankOrderAsc(boolean active, Long agentId);

    @Query(value = "select o from Bank o where o.wallet.size>0 and o.agent.id = :agentId")
    List<Bank> findUsageBank(Long agentId);

    Optional<Bank> findFirstByActiveAndBankGroupAndBankOrderAndAgent_IdGreaterThanOrderByBankOrderAsc(boolean active, int bankGroupFrom, int bankOrderFrom, Long agentId);


    Optional<Bank> findFirstByActiveAndBankGroupAndAgent_IdGreaterThanOrderByBankGroupAsc(boolean active, int bankGroupFrom, Long agentId);


    Optional<Bank> findByBankTypeAndBotIpAndAgent_Id(String bankType, String botIp, Long agentId);

    List<Bank> findByBankTypeAndActiveAndAgent_IdOrderByBankGroupAscBankOrderAsc(String bankType, Boolean isActive, Long agentId);

    List<Bank> findByBankTypeAndActive(String bankType, Boolean active);
}
