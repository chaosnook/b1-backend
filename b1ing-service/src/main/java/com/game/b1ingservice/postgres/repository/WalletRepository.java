package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet> {
    List<Wallet> findByBankIsNull();
    List<Wallet> findByTrueWalletIsNull();
}
