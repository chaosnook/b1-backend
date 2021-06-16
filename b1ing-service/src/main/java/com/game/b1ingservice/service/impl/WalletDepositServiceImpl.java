package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.walletdeposit.WalletDepositAllRequest;
import com.game.b1ingservice.payload.walletdeposit.WalletDepositList;
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

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        Optional<TrueWallet> trueWallet = trueWalletRepository.findFirstByIdAndActiveAndAgent_Id(request.getTrueWalletId(), true, request.getAgentId());
        if (!trueWallet.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_04000);
        }

        Optional<WebUser> webUser = webUserRepository.findFirstByUsernameAndAgent_Id(request.getUsername(), request.getAgentId());
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
        Optional<TrueWallet> trueWalletFrom = trueWalletRepository.findFirstByIdAndActiveAndAgent_Id(request.getTrueWalletIdFrom(), true, request.getAgentId());
        if (!trueWalletFrom.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_04000);
        }
        Optional<TrueWallet> trueWalletTo = trueWalletRepository.findFirstByIdAndActiveAndAgent_Id(request.getTrueWalletIdTo(), true, request.getAgentId());
        if (!trueWalletTo.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_04000);
        }
        walletRepository.updateAllTrueWalletDeposit(trueWalletTo.get().getId(), trueWalletFrom.get().getId());
    }

    @Override
    public List<WalletDepositList> findActiveWallet(Long agentId) {
        return trueWalletRepository.findAllByAgent_IdAndActiveOrderByBankGroupAsc(agentId, true).stream().map(converterActive).collect(Collectors.toList());
    }

    private final Function<TrueWallet, WalletDepositList> converterActive = wallet -> {
        WalletDepositList walletRes = new WalletDepositList();
        walletRes.setId(wallet.getId());
        walletRes.setName(wallet.getName());
        walletRes.setBankGroup(wallet.getBankGroup());
        walletRes.setPrefix(wallet.getPrefix());
        walletRes.setPhoneNumber(wallet.getPhoneNumber());
        return walletRes;
    };

    private final Function<Wallet, WalletDepositResponse> converter = wallet -> {
        WalletDepositResponse agentResponse = new WalletDepositResponse();
        agentResponse.setId(wallet.getId());
        agentResponse.setUsername(wallet.getUser().getUsername());
        agentResponse.setBankGroup(wallet.getTrueWallet().getBankGroup());
        agentResponse.setTrueWalletId(wallet.getTrueWallet().getId());
        return agentResponse;
    };
}
