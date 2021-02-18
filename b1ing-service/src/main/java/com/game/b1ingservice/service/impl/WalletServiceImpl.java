package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.amb.AmbResponse;
import com.game.b1ingservice.payload.amb.GetCreditRes;
import com.game.b1ingservice.payload.userinfo.UserWalletResponse;
import com.game.b1ingservice.payload.wellet.WalletRequest;
import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.entity.TrueWallet;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.repository.BankRepository;
import com.game.b1ingservice.postgres.repository.TrueWalletRepository;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.service.AMBService;
import com.game.b1ingservice.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private TrueWalletRepository trueWalletRepository;

    @Autowired
    private AMBService ambService;

    @Override
    public void createWallet(WalletRequest req, WebUser user) {

        Wallet wallet = new Wallet();
        wallet.setCredit(req.getCredit());
        wallet.setPoint(req.getPoint());
        wallet.setTurnOver(BigDecimal.ZERO);
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

        Wallet wallet = walletRepository.findFirstByUser_UsernameAndUser_Agent_Prefix(username, prefix);
        if (wallet == null) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00007);
        }
        AmbResponse<GetCreditRes> ambRes = ambService.getCredit(username);
        if (ambRes.getCode() != 0) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00007);
        }

        BigDecimal credit = ambRes.getResult().getCredit();

        WebUser webUser = wallet.getUser();

        walletRepository.updateUserCredit(credit, webUser.getId());

        UserWalletResponse response = new UserWalletResponse();
        response.setCredit(wallet.getCredit());
        response.setPoint(credit);
        response.setHasTrueWallet(wallet.getTrueWallet() != null);
        response.setHasBank(wallet.getBank() != null);

        return response;
    }
}
