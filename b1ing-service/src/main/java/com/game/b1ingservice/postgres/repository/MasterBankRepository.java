package com.game.b1ingservice.postgres.repository;

import com.game.b1ingservice.postgres.entity.Config;
import com.game.b1ingservice.postgres.entity.MasterBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterBankRepository extends JpaRepository<MasterBank, Long>, JpaSpecificationExecutor<MasterBank> {

}
