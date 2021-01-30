package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.walletdeposit.WalletDepositAllRequest;
import com.game.b1ingservice.payload.walletdeposit.WalletDepositRequest;
import com.game.b1ingservice.payload.walletdeposit.WalletDepositResponse;
import com.game.b1ingservice.postgres.entity.TrueWallet;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.repository.TrueWalletRepository;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.service.WalletDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class WalletDepositServiceImpl implements WalletDepositService {

    private final WalletRepository walletRepository;

    private final TrueWalletRepository trueWalletRepository;

    private final WebUserRepository webUserRepository;

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

    @Override
    public void updateTrueWalletDeposit(WalletDepositRequest request) {

        Optional<TrueWallet> trueWallet = trueWalletRepository.findFirstByBankGroupAndPrefixAndActive(request.getBankGroup(), request.getPrefix(), true);
        if (!trueWallet.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_04000);
        }

        Optional<WebUser> webUser = webUserRepository.findFirstByUsernameAndAgent_Prefix(request.getUsername(), request.getPrefix());
        if (!webUser.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01127);
        }

        int row = walletRepository.updateTrueWalletDeposit(trueWallet.get().getId(), webUser.get().getId());
        if (row == 0) {
            throw new ErrorMessageException(Constants.ERROR.ERR_04001);
        }
    }

    @Override
    public void updateAllTrueWalletDeposit(WalletDepositAllRequest request) {
        Optional<TrueWallet> trueWalletFrom = trueWalletRepository.findFirstByBankGroupAndPrefixAndActive(request.getBankGroupFrom(), request.getPrefix(), true);
        if (!trueWalletFrom.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_04000);
        }
        Optional<TrueWallet> trueWalletTo = trueWalletRepository.findFirstByBankGroupAndPrefixAndActive(request.getBankGroupTo(), request.getPrefix(), true);
        if (!trueWalletTo.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_04000);
        }
        walletRepository.updateAllTrueWalletDeposit(trueWalletTo.get().getId(), trueWalletFrom.get().getId());
    }

    private final Function<Wallet, WalletDepositResponse> converter = wallet -> {
        WalletDepositResponse agentResponse = new WalletDepositResponse();
        agentResponse.setId(wallet.getId());
        agentResponse.setUsername(wallet.getUser().getUsername());
        agentResponse.setBankGroup(wallet.getTrueWallet().getBankGroup());
        return agentResponse;
    };
}
