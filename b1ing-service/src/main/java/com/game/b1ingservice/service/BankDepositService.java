package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.bankdeposit.BankDepositAllRequest;
import com.game.b1ingservice.payload.bankdeposit.BankDepositList;
import com.game.b1ingservice.payload.bankdeposit.BankDepositRequest;
import com.game.b1ingservice.payload.bankdeposit.BankDepositResponse;
import com.game.b1ingservice.postgres.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface BankDepositService {

    List<BankDepositList> listActiveBank(Long agentId);

    List<BankDepositList> listUsageBank(Long agentId);

    Page<BankDepositResponse> findByCriteria(Specification<Wallet> specification, Pageable pageable);

    BankDepositRequest checkSortField(BankDepositRequest request);

    void updateBankDeposit(BankDepositRequest request, Long agentId);

    void updateAllBankDeposit(BankDepositAllRequest request, Long agentId);
}
