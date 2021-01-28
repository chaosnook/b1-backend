package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.wellet.WalletRequest;
import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.entity.TrueWallet;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.repository.BankRepository;
import com.game.b1ingservice.postgres.repository.TrueWalletRepository;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private TrueWalletRepository trueWalletRepository;

    @Override
    public void createWallet(WalletRequest req) {

        Wallet wallet = new Wallet();
        wallet.setCredit(req.getCredit());
        wallet.setPoint(req.getPoint());

        Optional<Bank> optionalBank = bankRepository.findFirstByActiveOrderByBankGroupAscBankOrderAsc(true);
        if(optionalBank.isPresent()) {
            Bank bank = optionalBank.get();
            wallet.setBank(bank);
        }

        Optional<TrueWallet> optionalTrueWallet = trueWalletRepository.findFirstByActiveOrderByBankGroupAsc(true);
        if(optionalTrueWallet.isPresent()) {
            TrueWallet trueWallet = optionalTrueWallet.get();
            wallet.setTrueWallet(trueWallet);
        }

        walletRepository.save(wallet);

    }
}
