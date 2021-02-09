package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.entity.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET deposit_bank_id = ? WHERE user_id = ? ", nativeQuery = true)
    int updateBankDeposit(Long bankId, Long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET deposit_bank_id = ? WHERE deposit_bank_id = ? ", nativeQuery = true)
    int updateAllBankDeposit(Long bankIdTo, Long bankIdFrom);

    Wallet findFirstByUser_UsernameAndUser_Agent_Prefix(String username , String prefix);

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET deposit_bank_id = null WHERE deposit_bank_id = ? ", nativeQuery = true)
    int deleteAllBankDeposit(Long bankIdFrom);


    List<Wallet> findByBankIsNull();
    List<Wallet> findByTrueWalletIsNull();
}
