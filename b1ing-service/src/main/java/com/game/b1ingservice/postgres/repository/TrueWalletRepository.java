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

    boolean existsByBankGroupAndAgent_Id(int bankGroup, Long agentId);

    Optional<TrueWallet> findFirstByActiveAndAgent_IdOrderByBankGroupAsc(boolean active, Long agentId);

    Optional<TrueWallet> findFirstByIdAndPrefixAndActive(Long id, String prefix, boolean active);

    Optional<TrueWallet> findFirstByIdAndAgent_Id(Long id, Long agentId);

    List<TrueWallet> findAllByAgent_IdOrderByIdDesc(Long agentId, Sort sort);

    List<TrueWallet> findAllByPrefixAndActiveOrderByBankGroupAsc(String prefix, boolean active);

    Optional<TrueWallet> findFirstByActiveAndBankGroupAndAgent_IdGreaterThanOrderByBankGroupAsc(boolean active, int bankGroupFrom, Long agentId);

    Optional<TrueWallet> findByBotIpAndAgent_Id(String botIp, Long agentId);


    Optional<TrueWallet> findFirstByIdAndActiveAndAgent_Id(Long id, boolean active, Long agentId);

    List<TrueWallet> findAllByAgent_IdAndActiveOrderByBankGroupAsc(Long agentId, boolean active);
}
