package com.game.b1ingservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.amb.AmbResponse;
import com.game.b1ingservice.payload.amb.DepositReq;
import com.game.b1ingservice.payload.amb.DepositRes;
import com.game.b1ingservice.payload.bankbot.*;
import com.game.b1ingservice.payload.promotion.PromotionEffectiveRequest;
import com.game.b1ingservice.postgres.entity.*;
import com.game.b1ingservice.postgres.repository.*;
import com.game.b1ingservice.service.AMBService;
import com.game.b1ingservice.service.AffiliateService;
import com.game.b1ingservice.service.BankBotService;
import com.game.b1ingservice.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
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

    @Autowired
    private AMBService ambService;

    @Autowired
    private OkHttpClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private AffiliateService affiliateService;

    @Autowired
    private WebUserRepository webUserRepository;

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");
    @Override
    public void addCredit(BankBotAddCreditRequest request) {
        if (!depositHistoryRepository.existsByTransactionId(request.getTransactionId())) {
            Optional<Bank> opt = bankRepository.findByBankTypeAndBotIp("DEPOSIT",request.getBotIp());

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
//           todo must map to promotion here
//            and set
            depositHistory.setBonusAmount(BigDecimal.ZERO);

            if (wallets.size() == 1) {
                Wallet wallet = wallets.get(0);
                depositHistory.setBeforeAmount(wallet.getCredit());
                depositHistory.setAfterAmount(wallet.getCredit().add(request.getAmount()));
                depositHistory.setUser(wallet.getUser());
                depositHistory.setBank(wallet.getBank());
                BigDecimal bonus = mapPromotion(depositHistory,request.getTransactionDate());
                depositHistory.setBonusAmount(bonus);
                try {
                    AmbResponse<DepositRes> result = sendToAskMeBet(depositHistory,wallet);
                    if (0 == result.getCode()){
                        depositHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);
                        walletRepository.depositCredit(request.getAmount(), wallet.getUser().getId());
                        webUserRepository.updateDepositRef(result.getResult().getRef(), wallet.getUser().getId());
                        // check affiliate
                        affiliateService.earnPoint(wallet.getUser().getId(), request.getAmount(), wallet.getUser().getAgent().getPrefix());

                    }else {
                        depositHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                        depositHistory.setReason("Can't add credit at amb api");
                    }

                } catch (Exception e) {
                    depositHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                    depositHistory.setReason(e.getLocalizedMessage());
                    log.error("BankBotService addCredit error : {}", e);
                }
            } else {
                depositHistory.setStatus(Constants.DEPOSIT_STATUS.PENDING);
                depositHistory.setReason("Not sure which wallet");
                BigDecimal bonus = mapPromotion(depositHistory,request.getTransactionDate());
                depositHistory.setBonusAmount(bonus);
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
//          todo must map to promotion here
//            and set
            depositHistory.setBonusAmount(BigDecimal.ZERO);
            if (wallets.size() == 1) {
                Wallet wallet = wallets.get(0);
                depositHistory.setBeforeAmount(wallet.getCredit());
                depositHistory.setAfterAmount(wallet.getCredit().add(request.getAmount()));
                depositHistory.setUser(wallet.getUser());
                depositHistory.setTrueWallet(wallet.getTrueWallet());
                depositHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);
                BigDecimal bonus = mapPromotion(depositHistory,request.getTransactionDate());
                depositHistory.setBonusAmount(bonus);
                try {

                    AmbResponse<DepositRes> result = sendToAskMeBet(depositHistory,wallet);
                    if (0 == result.getCode()){
                        depositHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);
                        walletRepository.depositCredit(request.getAmount(), wallet.getUser().getId());
                        webUserRepository.updateDepositRef(result.getResult().getRef(), wallet.getUser().getId());
                        // check affiliate
                        affiliateService.earnPoint(wallet.getUser().getId(), request.getAmount(), wallet.getUser().getAgent().getPrefix());

                    }else {
                        depositHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                        depositHistory.setReason("Can't add credit at amb api");
                    }
                } catch (Exception e) {
                    depositHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                    depositHistory.setReason(e.getLocalizedMessage());
                    log.error("BankBotService addTrueCredit error : {}", e);
                }
            } else {
                depositHistory.setStatus(Constants.DEPOSIT_STATUS.PENDING);
                depositHistory.setReason("Not sure which wallet");
                BigDecimal bonus = mapPromotion(depositHistory,request.getTransactionDate());
                depositHistory.setBonusAmount(bonus);
                if (opt.isPresent()){
                    depositHistory.setTrueWallet(opt.get());
                }
            }
            depositHistoryRepository.save(depositHistory);
        }
    }

    @Override
    public BankBotScbWithdrawCreditResponse withDrawCredit(BankBotScbWithdrawCreditRequest request) {
        List<Bank> banks = bankRepository.findByBankTypeAndActiveOrderByBankGroupAscBankOrderAsc("WITHDRAW", true);
        if (0<banks.size()){
            Bank bank = banks.get(0);

            try {
                RequestBody body = RequestBody.create(objectMapper.writeValueAsString(request), MEDIA_JSON);
                Request bankBotRequest = new Request.Builder()
                        .url(String.format("http://%s/scb/api/withdraw.php", bank.getBotIp()))
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();

                Response response = client.newCall(bankBotRequest).execute();

                return objectMapper.readValue(response.body().string(), new TypeReference<BankBotScbWithdrawCreditResponse>() {
                });
            }catch (Exception e){
                BankBotScbWithdrawCreditResponse response = new BankBotScbWithdrawCreditResponse();
                response.setStatus(false);
                response.setMessege(e.getLocalizedMessage());
                return response;
            }
        }
        BankBotScbWithdrawCreditResponse response = new BankBotScbWithdrawCreditResponse();
        response.setStatus(false);
        response.setMessege("Not found withdraw bank");
        return response;
    }

    private AmbResponse<DepositRes> sendToAskMeBet(DepositHistory depositHistory,Wallet wallet){
        DepositReq depositReq = DepositReq.builder().amount(depositHistory.getAmount().setScale(2, RoundingMode.HALF_DOWN).toPlainString()).build();
        return ambService.deposit(depositReq, wallet.getUser().getUsername(),wallet.getUser().getAgent());
    }
    private BigDecimal mapPromotion(DepositHistory depositHistory, Instant transactionDate){
        PromotionEffectiveRequest effectiveRequest = new PromotionEffectiveRequest();
        effectiveRequest.setAmount(depositHistory.getAmount());
        effectiveRequest.setTransactionId(depositHistory.getTransactionId());
        effectiveRequest.setTransactionDate(transactionDate);
        effectiveRequest.setUser(depositHistory.getUser());
        BigDecimal bonus = BigDecimal.ZERO;
        List<Promotion> promotionList = promotionService.getEffectivePromotion(effectiveRequest);

        List<PromotionHistory> promotionHistories = new ArrayList<>();

        for (Promotion promotion : promotionList) {
            PromotionHistory history = promotionService.calculatePromotionBonus(promotion, effectiveRequest);
            bonus.add(history.getBonus());
            promotionHistories.add(history);
        }
        return bonus;
    }
}
