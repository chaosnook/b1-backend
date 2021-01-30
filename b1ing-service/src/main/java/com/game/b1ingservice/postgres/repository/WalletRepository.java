package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET deposit_true_wallet_id = ? WHERE user_id = ? ", nativeQuery = true)
    int updateTrueWalletDeposit(Long trueWallet, Long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET deposit_true_wallet_id = ? WHERE deposit_true_wallet_id = ? ", nativeQuery = true)
    int updateAllTrueWalletDeposit(Long trueWalletTo, Long trueWalletFrom);
}
