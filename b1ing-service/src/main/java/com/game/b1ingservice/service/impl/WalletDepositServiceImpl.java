package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.walletdeposit.WalletDepositRequest;
import com.game.b1ingservice.payload.walletdeposit.WalletDepositResponse;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.service.WalletDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class WalletDepositServiceImpl implements WalletDepositService {

    private final WalletRepository walletRepository;

    @Override
    public Page<WalletDepositResponse> findByCriteria(Specification<Wallet> specification, Pageable pageable) {
        return walletRepository.findAll(specification, pageable).map(converter);
    }

    @Override
    public WalletDepositRequest checkSortField(WalletDepositRequest request) {
        if (request.getSortField().equals("username")) {
            request.setSortField("user.username");
        } else if (request.getSortField().equals("bankGroup")) {
            request.setSortField("trueWallet.bankGroup");
        }
        return request;
    }

    private final Function<Wallet, WalletDepositResponse> converter = wallet -> {
        WalletDepositResponse agentResponse = new WalletDepositResponse();
        agentResponse.setId(wallet.getId());
        agentResponse.setUsername(wallet.getUser().getUsername());
        agentResponse.setBankGroup(wallet.getTrueWallet().getBankGroup());
        return agentResponse;
    };
}
