package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.userinfo.UserWalletResponse;
import com.game.b1ingservice.payload.wellet.WalletRequest;
import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.entity.TrueWallet;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.entity.WebUser;
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
    public void createWallet(WalletRequest req, WebUser user) {

        Wallet wallet = new Wallet();
        wallet.setCredit(req.getCredit());
        wallet.setPoint(req.getPoint());
        wallet.setUser(user);

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

    @Override
    public UserWalletResponse getUserWallet(String username, String prefix) {

        //TODO get credit from AMB
       Wallet wallet = walletRepository.findFirstByUser_UsernameAndUser_Agent_Prefix(username, prefix);
       if (wallet == null) {
           throw new ErrorMessageException(Constants.ERROR.ERR_00007);
       }
        UserWalletResponse response = new UserWalletResponse();
        response.setCredit(wallet.getCredit());
        response.setPoint(wallet.getPoint());

        return null;
    }
}
