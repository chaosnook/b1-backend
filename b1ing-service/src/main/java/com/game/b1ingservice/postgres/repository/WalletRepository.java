package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
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

    Wallet findFirstByUser_UsernameAndUser_Agent_Id(String username , Long agentId);

    Wallet findFirstByUser_IdAndUser_Agent_Id(Long userId , Long agentId);

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
    @Query(value = "UPDATE wallet SET credit = credit + ? ,turn_over = turn_over + ? , version=version+1  WHERE user_id = ? ", nativeQuery = true)
    int depositCreditAndTurnOverNonMultiply(BigDecimal credit,BigDecimal creditMul, Long userId);


    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET credit = credit + ? ,turn_over = turn_over + (?*?) , version=version+1  WHERE user_id = ? ", nativeQuery = true)
    int depositCreditAndTurnOver(BigDecimal credit,BigDecimal creditMul, Integer turnOver, Long userId);

    @Query(value = "select o from Wallet o where o.bank.botIp = :botIp and o.user.accountNumber like :accountNumber and o.user.bankName = :bankName and o.user.agent.id = :agentId")
    List<Wallet> findWalletLikeAccount(String botIp, String accountNumber, String bankName, Long agentId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET credit = ? , version=version+1, updated_date = now()  WHERE user_id = ? ", nativeQuery = true)
    void updateUserCredit(BigDecimal credit, Long userId);

    @Query(value = "select o from Wallet o where o.trueWallet.botIp = :botIp and o.user.tel = :mobile and o.user.agent.id = :agentId")
    List<Wallet> findWalletByTrueMobile(String botIp, String mobile, Long agentId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE wallet SET turn_over = turn_over - ?  WHERE user_id = ? ", nativeQuery = true)
    void minusTurnOver(BigDecimal afterAmount, Long id);

    @Query(value = "select turn_over from wallet where  user_id = ?" , nativeQuery = true)
    BigDecimal getTurnOver(Long id);
}
