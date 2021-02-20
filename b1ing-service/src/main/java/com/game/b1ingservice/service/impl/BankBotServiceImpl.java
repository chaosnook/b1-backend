package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditRequest;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditTrueWalletRequest;
import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.postgres.entity.TrueWallet;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.repository.BankRepository;
import com.game.b1ingservice.postgres.repository.DepositHistoryRepository;
import com.game.b1ingservice.postgres.repository.TrueWalletRepository;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.service.BankBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BankBotServiceImpl implements BankBotService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private TrueWalletRepository trueWalletRepository;

    @Override
    public void addCredit(BankBotAddCreditRequest request) {
        if (!depositHistoryRepository.existsByTransactionId(request.getTransactionId())) {
            Optional<Bank> opt = bankRepository.findByBotIp(request.getBotIp());

            String accountLike = "%" + request.getAccountNo();

            List<Wallet> wallets = walletRepository.findWalletLikeAccount(request.getBotIp(),accountLike);

            DepositHistory depositHistory = new DepositHistory();
            depositHistory.setAmount(request.getAmount());
            depositHistory.setTransactionId(request.getTransactionId());
            depositHistory.setFirstName(request.getFirstName());
            depositHistory.setLastName(request.getLastName());
            depositHistory.setRemark(request.getRemark());
            depositHistory.setType(Constants.DEPOSIT_TYPE.BANK);
            depositHistory.setIsAuto(true);
//            must map to promotion here
//            and set
            depositHistory.setBonusAmount(BigDecimal.ZERO);

            if (wallets.size() == 1) {
                Wallet wallet = wallets.get(0);
                depositHistory.setBeforeAmount(wallet.getCredit());
                depositHistory.setAfterAmount(wallet.getCredit().add(request.getAmount()));
                depositHistory.setUser(wallet.getUser());
                depositHistory.setBank(wallet.getBank());
                depositHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);
                try {
                    walletRepository.depositCredit(request.getAmount(), wallet.getUser().getId());
                } catch (Exception e) {
                    depositHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                    depositHistory.setReason(e.getLocalizedMessage());
                    log.error("BankBotService addCredit error : {}", e);
                }
            } else {
                depositHistory.setStatus(Constants.DEPOSIT_STATUS.PENDING);
                depositHistory.setReason("Not sure which wallet");
                if (opt.isPresent()){
                    depositHistory.setBank(opt.get());
                }
            }
            depositHistoryRepository.save(depositHistory);
        }
    }

    @Override
    public void addCreditTrue(BankBotAddCreditTrueWalletRequest request) {
        if (!depositHistoryRepository.existsByTransactionId(request.getTransactionId())) {
            Optional<TrueWallet> opt = trueWalletRepository.findByBotIp(request.getBotIp());

            List<Wallet> wallets = walletRepository.findWalletByTrueMobile(request.getBotIp(), request.getMobile());

            DepositHistory depositHistory = new DepositHistory();
            depositHistory.setAmount(request.getAmount());
            depositHistory.setTransactionId(request.getTransactionId());
            depositHistory.setMobile(request.getMobile());
            depositHistory.setIsAuto(true);
            depositHistory.setType(Constants.DEPOSIT_TYPE.TRUEWALLET);
            depositHistory.setRemark(Date.from(request.getTransactionDate()).toString());
//            must map to promotion here
//            and set
            depositHistory.setBonusAmount(BigDecimal.ZERO);
            if (wallets.size() == 1) {
                Wallet wallet = wallets.get(0);
                depositHistory.setBeforeAmount(wallet.getCredit());
                depositHistory.setAfterAmount(wallet.getCredit().add(request.getAmount()));
                depositHistory.setUser(wallet.getUser());
                depositHistory.setTrueWallet(wallet.getTrueWallet());
                depositHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);
                try {
                    walletRepository.depositCredit(request.getAmount(), wallet.getUser().getId());
                } catch (Exception e) {
                    depositHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                    depositHistory.setReason(e.getLocalizedMessage());
                    log.error("BankBotService addTrueCredit error : {}", e);
                }
            } else {
                depositHistory.setStatus(Constants.DEPOSIT_STATUS.PENDING);
                depositHistory.setReason("Not sure which wallet");
                if (opt.isPresent()){
                    depositHistory.setTrueWallet(opt.get());
                }
            }
            depositHistoryRepository.save(depositHistory);
        }
    }
}
