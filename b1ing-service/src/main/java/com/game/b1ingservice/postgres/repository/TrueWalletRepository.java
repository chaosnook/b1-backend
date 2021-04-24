package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.TrueWallet;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrueWalletRepository extends JpaRepository<TrueWallet, Long>, JpaSpecificationExecutor<TrueWallet> {
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByBankGroup(int bankGroup);

    Optional<TrueWallet> findFirstByActiveOrderByBankGroupAsc(boolean active);

    Optional<TrueWallet> findFirstByIdAndPrefixAndActive(Long id, String prefix, boolean active);

    Optional<TrueWallet> findFirstByIdAndPrefix(Long id, String prefix);

    List<TrueWallet> findAllByPrefixOrderByIdDesc(String prefix, Sort sort);

    List<TrueWallet> findAllByPrefixAndActiveOrderByBankGroupAsc(String prefix, boolean active);

    Optional<TrueWallet> findFirstByActiveAndBankGroupGreaterThanOrderByBankGroupAsc(boolean active, int bankGroupFrom);

    Optional<TrueWallet> findByBotIp(String botIp);
}
