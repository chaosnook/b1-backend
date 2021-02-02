package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.entity.TrueWallet;
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

    Optional<Bank> findFirstByActiveAndBankCodeAndBankGroupOrderByBankGroupAscBankOrderAsc(boolean active, String bankCode, int bankGroup);

    List<Bank> findAllByActiveOrderByBankGroupAscBankOrderAsc(boolean active);

    @Query(value = "select o from Bank o where o.wallet.size>0")
    List<Bank> findUsageBank();

}
