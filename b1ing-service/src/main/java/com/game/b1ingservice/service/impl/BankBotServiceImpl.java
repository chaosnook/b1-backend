package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditRequest;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.repository.DepositHistoryRepository;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.service.BankBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BankBotServiceImpl implements BankBotService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;

    @Override
    public void addCredit(BankBotAddCreditRequest request) {
        if (!depositHistoryRepository.existsByTransactionId(request.getTransactionId())) {
            String accountLike = "%" + request.getAccountNo();
            List<Wallet> wallets = walletRepository.findWalletLikeAccount(accountLike);

            DepositHistory depositHistory = new DepositHistory();
            depositHistory.setAmount(request.getAmount());
            depositHistory.setTransactionId(request.getTransactionId());
            depositHistory.setFirstName(request.getFirstName());
            depositHistory.setLastName(request.getLastName());
            depositHistory.setRemark(request.getRemark());
            depositHistory.setType(Constants.DEPOSIT_TYPE.BANK);
//            must map to promotion here
//            and set
//            depositHistory.setBonusAmount();

            if (wallets.size() == 1) {
                Wallet wallet = wallets.get(0);
                depositHistory.setBeforeAmount(wallet.getCredit());
                depositHistory.setAfterAmount(wallet.getCredit().add(request.getAmount()));
                depositHistory.setUser(wallet.getUser());
                depositHistory.setBank(wallet.getBank());
                depositHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);
                depositHistory.setIsAuto(true);
                try {
                    walletRepository.depositCredit(request.getAmount(), wallet.getUser().getId());
                } catch (Exception e) {
                    depositHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                    log.error("BankBotService addCredit error : {}", e);
                }
            } else {
                depositHistory.setIsAuto(false);
                depositHistory.setStatus(Constants.DEPOSIT_STATUS.PENDING);
            }
            depositHistoryRepository.save(depositHistory);
        }
    }
}
