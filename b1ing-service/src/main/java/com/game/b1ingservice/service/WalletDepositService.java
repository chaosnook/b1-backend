package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.walletdeposit.WalletDepositAllRequest;
import com.game.b1ingservice.payload.walletdeposit.WalletDepositList;
import com.game.b1ingservice.payload.walletdeposit.WalletDepositRequest;
import com.game.b1ingservice.payload.walletdeposit.WalletDepositResponse;
import com.game.b1ingservice.postgres.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface WalletDepositService {

    Page<WalletDepositResponse> findByCriteria(Specification<Wallet> specification, Pageable pageable);

    WalletDepositRequest checkSortField(WalletDepositRequest request);

    void updateTrueWalletDeposit(WalletDepositRequest request);

    void updateAllTrueWalletDeposit(WalletDepositAllRequest request);

    List<WalletDepositList> findActiveWallet(Long agentId);
}
