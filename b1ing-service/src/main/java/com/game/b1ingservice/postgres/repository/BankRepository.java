package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.entity.TrueWallet;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long>, JpaSpecificationExecutor<Bank> {
    Optional<Bank> findById(Long id);
    boolean existsByBankOrder(int bankGroup);
    boolean existsByBankGroup(int bankOrder);
    Optional<Bank> findFirstByActiveOrderByBankGroupAscBankOrderAsc(boolean active);

    List<Bank> findAllByActiveOrderByBankGroupAscBankOrderAsc(boolean active);

    @Query(value = "select o from Bank o where o.wallet.size>0")
    List<Bank> findUsageBank();

    Optional<Bank> findFirstByActiveAndBankGroupAndBankOrderGreaterThanOrderByBankOrderAsc(boolean active, int bankGroupFrom, int bankOrderFrom);

    Optional<Bank> findFirstByActiveAndBankGroupGreaterThanOrderByBankGroupAsc(boolean active, int bankGroupFrom);


    Optional<Bank> findByBotIp(String botIp);
}
