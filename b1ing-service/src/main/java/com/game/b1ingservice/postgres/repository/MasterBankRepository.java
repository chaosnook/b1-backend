package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Config;
import com.game.b1ingservice.postgres.entity.MasterBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterBankRepository extends JpaRepository<MasterBank, Long>, JpaSpecificationExecutor<MasterBank> {
 List<MasterBank> findAllByIsDeposit(boolean isDeposit);
 List<MasterBank> findAllByIsWithdraw(boolean isWithdraw);
 List<MasterBank> findAllByIsUserBank(boolean isUserBank);
}
