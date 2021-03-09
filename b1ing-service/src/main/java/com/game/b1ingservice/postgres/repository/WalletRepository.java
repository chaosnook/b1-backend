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

import java.math.BigDecimal;
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

    Wallet findFirstByUser_IdAndUser_Agent_Prefix(Long userId , String prefix);

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET deposit_bank_id = null WHERE deposit_bank_id = ? ", nativeQuery = true)
    int deleteAllBankDeposit(Long bankIdFrom);

    Optional<Wallet> findByUser_Id(Long id);


    List<Wallet> findByBankIsNull();
    List<Wallet> findByTrueWalletIsNull();

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET credit = credit + ? ,point = point - ? ,turn_over = turn_over + (?*?) , version=version+1 WHERE user_id = ? ", nativeQuery = true)
    int transferPointToCredit(BigDecimal point, BigDecimal point2, BigDecimal point3, Integer turnover, Long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET point = point + ?, version=version+1  WHERE user_id = ? ", nativeQuery = true)
    int earnPoint(BigDecimal point, Long userId) ;

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET credit = credit - ? , version=version+1  WHERE user_id = ? ", nativeQuery = true)
    int withDrawCredit(BigDecimal credit, Long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET credit = credit + ? , version=version+1  WHERE user_id = ? ", nativeQuery = true)
    int depositCredit(BigDecimal credit, Long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET credit = credit + ? ,turn_over = turn_over + (?*?) , version=version+1  WHERE user_id = ? ", nativeQuery = true)
    int depositCreditAndTurnOver(BigDecimal credit,BigDecimal creditMul, Integer turnOver, Long userId);

    @Query(value = "select o from Wallet o where o.bank.botIp = :botIp and o.user.accountNumber like :accountNumber")
    List<Wallet> findWalletLikeAccount(String botIp, String accountNumber);

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET credit = ? , version=version+1  WHERE user_id = ? ", nativeQuery = true)
    void updateUserCredit(BigDecimal credit, Long userId);

    @Query(value = "select o from Wallet o where o.trueWallet.botIp = :botIp and o.user.tel = :mobile")
    List<Wallet> findWalletByTrueMobile(String botIp, String mobile);
}
