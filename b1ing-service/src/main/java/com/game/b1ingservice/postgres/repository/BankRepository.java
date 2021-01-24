package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long>, JpaSpecificationExecutor<Bank> {
    Optional<Bank> findById(Long id);
    boolean existsByBankCode(String bankCode);
}