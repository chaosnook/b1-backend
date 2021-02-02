package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.bankdeposit.BankDepositAllRequest;
import com.game.b1ingservice.payload.bankdeposit.BankDepositList;
import com.game.b1ingservice.payload.bankdeposit.BankDepositRequest;
import com.game.b1ingservice.payload.bankdeposit.BankDepositResponse;
import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.entity.TrueWallet;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.repository.BankRepository;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.service.BankDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BankDepositServiceImpl implements BankDepositService {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private WebUserRepository webUserRepository;

    @Override
    public List<BankDepositList> listActiveBank() {
        List<BankDepositList> list = bankRepository.findAllByActiveOrderByBankGroupAscBankOrderAsc(true).stream().map(converterBank).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<BankDepositList> listUsageBank() {
        List<BankDepositList> list = bankRepository.findUsageBank().stream().map(converterBank).collect(Collectors.toList());
        return list;
    }

    @Override
    public Page<BankDepositResponse> findByCriteria(Specification<Wallet> specification, Pageable pageable) {
        return walletRepository.findAll(specification, pageable).map(converter);
    }

    @Override
    public BankDepositRequest checkSortField(BankDepositRequest request) {
        if (request.getSortField().equals("username")) {
            request.setSortField("user.username");
        } else if (request.getSortField().equals("bankGroup")) {
            request.setSortField("bank.bankGroup");
        } else if (request.getSortField().equals("bankOrder")){
            request.setSortField("bank.bankOrder");
        }
        return request;
    }

    @Override
    public void updateBankDeposit(BankDepositRequest request) {
        Optional<Bank> bank = bankRepository.findById(request.getBankId());
        if (!bank.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_04000);
        }

        Optional<WebUser> webUser = webUserRepository.findFirstByUsernameAndAgent_Prefix(request.getUsername(), request.getPrefix());
        if (!webUser.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01127);
        }

        int row = walletRepository.updateBankDeposit(bank.get().getId(), webUser.get().getId());
        if (row == 0) {
            throw new ErrorMessageException(Constants.ERROR.ERR_04001);
        }
    }

    @Override
    public void updateAllBankDeposit(BankDepositAllRequest request) {
        Optional<Bank> trueWalletFrom = bankRepository.findById(request.getBankIdFrom());
        if (!trueWalletFrom.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_04000);
        }
        Optional<Bank> trueWalletTo = bankRepository.findById(request.getBankIdTo());
        if (!trueWalletTo.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_04000);
        }
        walletRepository.updateAllBankDeposit(trueWalletTo.get().getId(), trueWalletFrom.get().getId());
    }

    private final Function<Wallet, BankDepositResponse> converter = wallet -> {
        BankDepositResponse agentResponse = new BankDepositResponse();
        agentResponse.setId(wallet.getId());
        agentResponse.setUsername(wallet.getUser().getUsername());
        agentResponse.setBankId(wallet.getBank().getId());
        agentResponse.setBankCode(wallet.getBank().getBankCode());
        agentResponse.setBankName(wallet.getBank().getBankName());
        agentResponse.setBankGroup(wallet.getBank().getBankGroup());
        agentResponse.setBankOrder(wallet.getBank().getBankOrder());
        return agentResponse;
    };

    private final Function<Bank, BankDepositList> converterBank = bank -> {
        BankDepositList res = new BankDepositList();
        res.setId(bank.getId());
        res.setBankCode(bank.getBankCode());
        res.setBankName(bank.getBankName());
        res.setBankGroup(bank.getBankGroup());
        res.setBankOrder(bank.getBankOrder());
        return res;
    };
}
