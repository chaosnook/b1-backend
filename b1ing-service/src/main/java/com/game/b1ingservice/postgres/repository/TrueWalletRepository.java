package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.TrueWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TrueWalletRepository extends JpaRepository<TrueWallet, Long>, JpaSpecificationExecutor<TrueWallet> {
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByBankGroup(int bankGroup);
}
