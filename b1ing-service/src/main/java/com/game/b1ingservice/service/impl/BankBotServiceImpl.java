package com.game.b1ingservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.amb.AmbResponse;
import com.game.b1ingservice.payload.amb.DepositReq;
import com.game.b1ingservice.payload.amb.DepositRes;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditRequest;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditTrueWalletRequest;
import com.game.b1ingservice.payload.bankbot.BankBotScbWithdrawCreditRequest;
import com.game.b1ingservice.payload.bankbot.BankBotScbWithdrawCreditResponse;
import com.game.b1ingservice.payload.promotion.PromotionEffectiveRequest;
import com.game.b1ingservice.payload.promotion.PromotionEffectiveResponse;
import com.game.b1ingservice.postgres.entity.*;
import com.game.b1ingservice.postgres.repository.*;
import com.game.b1ingservice.service.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.game.b1ingservice.commons.Constants.MESSAGE_DEPOSIT;
import static com.game.b1ingservice.commons.Constants.MESSAGE_DEPOSIT_BLOCK;

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

    @Autowired
    private LineNotifyService lineNotifyService;

    @Autowired
    private PromotionHistoryRepository promotionHistoryRepository;

    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");

    @Override
    public void addCredit(BankBotAddCreditRequest request, String prefix) {
        if (!depositHistoryRepository.existsByTransactionId(request.getTransactionId())) {
            Optional<Bank> opt = bankRepository.findByBankTypeAndBotIp("DEPOSIT", request.getBotIp());

            String accountLike = "%" + request.getAccountNo();

            List<Wallet> wallets = walletRepository.findWalletLikeAccount(request.getBotIp(), accountLike);

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

                WebUser webUser = wallet.getUser();

                if (webUser == null) {
                    log.error("addCredit : No user in wallet {}", wallet);
                    return;
                }

                depositHistory.setUser(webUser);
                depositHistory.setBank(wallet.getBank());

                BigDecimal turnOver = BigDecimal.ZERO;
                log.info("Add Credit status isBonus {} : blockBonus {}", webUser.getIsBonus(), webUser.getBlockBonus());

                // block bonus คือไม่แจกโบนัสให้ user นี้
                // is bonus คือลูกค้าต้องการโบนัส
                if (!webUser.getBlockBonus() && webUser.getIsBonus().equals("true")){
                    PromotionEffectiveResponse promotionBonus = mapPromotion(depositHistory, request.getTransactionDate());
                    depositHistory.setAfterAmount(wallet.getCredit().add(request.getAmount()).add(promotionBonus.getBonus()));
                    depositHistory.setBonusAmount(promotionBonus.getBonus());

                    turnOver = promotionBonus.getTurnOver();
                    depositHistory.setTurnOver(turnOver);
                } else {
                    depositHistory.setAfterAmount(wallet.getCredit().add(request.getAmount()));
                    depositHistory.setBonusAmount(BigDecimal.ZERO);
                    depositHistory.setTurnOver(BigDecimal.ZERO);
                }

                try {
                    // deposit auto คือ admin อนุญาติให้ลูกค้าคนนี้สามารถถอนเงินแบบ auto ได้
                    if (webUser.getDepositAuto()) {
                        AmbResponse<DepositRes> result = sendToAskMeBet(depositHistory, wallet);
                        log.info("amb response : {}", result);
                        if (0 == result.getCode()) {
                            depositHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);

                            lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_DEPOSIT,
                                    webUser.getUsername(),
                                    depositHistory.getAmount().setScale(2, RoundingMode.HALF_DOWN)),
                                    webUser.getAgent().getLineToken());

                            walletRepository.depositCreditAndTurnOverNonMultiply(depositHistory.getAmount().add(depositHistory.getBonusAmount()), turnOver, webUser.getId());
                            webUserRepository.updateDepositRef(result.getResult().getRef(), webUser.getId());
                            // check affiliate
                            affiliateService.earnPoint(webUser.getId(), request.getAmount(), webUser.getAgent().getPrefix());

                        } else {
                            depositHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                            depositHistory.setReason("Can't add credit at amb api");
                        }
                    } else {
                        depositHistory.setStatus(Constants.DEPOSIT_STATUS.BLOCK_AUTO);
                        depositHistory.setIsAuto(false);

                        lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_DEPOSIT_BLOCK, webUser.getUsername(),
                                depositHistory.getAmount().setScale(2, RoundingMode.HALF_DOWN)),
                                webUser.getAgent().getLineToken());

                    }

                } catch (Exception e) {
                    depositHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                    depositHistory.setReason(e.getLocalizedMessage());
                    log.error("BankBotService addCredit error : ", e);
                }
            } else {
                depositHistory.setStatus(Constants.DEPOSIT_STATUS.NOT_SURE);
                depositHistory.setReason("Not sure which wallet");
                PromotionEffectiveResponse promotionBonus = mapPromotion(depositHistory, request.getTransactionDate());
                depositHistory.setBonusAmount(promotionBonus.getBonus());
                depositHistory.setTurnOver(promotionBonus.getTurnOver());
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
                WebUser webUser = wallet.getUser();

                if (webUser == null) {
                    log.error("addCredit True : No user in wallet {}", wallet);
                    return;
                }

                depositHistory.setBeforeAmount(wallet.getCredit());
                depositHistory.setUser(webUser);
                depositHistory.setTrueWallet(wallet.getTrueWallet());

                depositHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);

                BigDecimal turnOver = BigDecimal.ZERO;
                log.info("Add Credit True status isBonus {} : blockBonus {}", webUser.getIsBonus(), webUser.getBlockBonus());

                // block bonus คือไม่แจกโบนัสให้ user นี้
                // is bonus คือลูกค้าต้องการโบนัส
                if (!webUser.getBlockBonus() && webUser.getIsBonus().equals("true")) {
                    PromotionEffectiveResponse promotionBonus = mapPromotion(depositHistory, request.getTransactionDate());
                    depositHistory.setAfterAmount(wallet.getCredit().add(request.getAmount()).add(promotionBonus.getBonus()));
                    depositHistory.setBonusAmount(promotionBonus.getBonus());

                    turnOver = promotionBonus.getTurnOver();
                    depositHistory.setTurnOver(turnOver);
                } else {
                    depositHistory.setAfterAmount(wallet.getCredit().add(request.getAmount()));
                    depositHistory.setBonusAmount(BigDecimal.ZERO);
                    depositHistory.setTurnOver(turnOver);
                }

                try {
                    // deposit auto คือ admin อนุญาติให้ลูกค้าคนนี้สามารถถอนเงินแบบ auto ได้
                    if (webUser.getDepositAuto()) {
                        AmbResponse<DepositRes> result = sendToAskMeBet(depositHistory, wallet);
                        log.info("amb response : {}", result);
                        if (0 == result.getCode()) {
                            depositHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);

                            lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_DEPOSIT,
                                    webUser.getUsername(),
                                    depositHistory.getAmount().setScale(2, RoundingMode.HALF_DOWN)),
                                    webUser.getAgent().getLineToken());

                            walletRepository.depositCreditAndTurnOverNonMultiply(depositHistory.getAmount().add(depositHistory.getBonusAmount()), turnOver, wallet.getUser().getId());
                            webUserRepository.updateDepositRef(result.getResult().getRef(), webUser.getId());
                            // check affiliate
                            affiliateService.earnPoint(webUser.getId(), request.getAmount(), webUser.getAgent().getPrefix());

                        } else {
                            depositHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                            depositHistory.setReason("Can't add credit at amb api");
                        }
                    } else {
                        depositHistory.setStatus(Constants.DEPOSIT_STATUS.BLOCK_AUTO);
                        depositHistory.setIsAuto(false);

                        lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_DEPOSIT_BLOCK, webUser.getUsername(),
                                depositHistory.getAmount().setScale(2, RoundingMode.HALF_DOWN)),
                                webUser.getAgent().getLineToken());

                    }
                } catch (Exception e) {
                    depositHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                    depositHistory.setReason(e.getLocalizedMessage());
                    log.error("BankBotService addTrueCredit error : ", e);
                }
            } else {
                depositHistory.setStatus(Constants.DEPOSIT_STATUS.NOT_SURE);
                depositHistory.setReason("Not sure which wallet");
                PromotionEffectiveResponse promotionBonus = mapPromotion(depositHistory,request.getTransactionDate());
                depositHistory.setBonusAmount(promotionBonus.getBonus());
                depositHistory.setTurnOver(promotionBonus.getTurnOver());
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
        if (!banks.isEmpty()){
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

    private AmbResponse<DepositRes> sendToAskMeBet(DepositHistory depositHistory, Wallet wallet){
        DepositReq depositReq = DepositReq.builder().amount(depositHistory.getAmount().setScale(2, RoundingMode.HALF_DOWN).toPlainString()).build();
        WebUser user = wallet.getUser();
        return ambService.deposit(depositReq, user.getUsernameAmb(), user.getAgent());
    }

    private PromotionEffectiveResponse mapPromotion(DepositHistory depositHistory, Instant transactionDate){
        PromotionEffectiveRequest effectiveRequest = new PromotionEffectiveRequest();
        effectiveRequest.setAmount(depositHistory.getAmount());
        effectiveRequest.setTransactionId(depositHistory.getTransactionId());
        effectiveRequest.setTransactionDate(transactionDate);
        effectiveRequest.setUser(depositHistory.getUser());

        BigDecimal bonus = BigDecimal.ZERO;
        BigDecimal turnOver = BigDecimal.ZERO;

        List<Promotion> promotionList = promotionService.getEffectivePromotion(effectiveRequest);

        List<PromotionHistory> promotionHistories = new ArrayList<>();

        for (Promotion promotion : promotionList) {
            PromotionHistory history = promotionService.calculatePromotionBonus(promotion, effectiveRequest);
            promotionHistories.add(history);
        }

        // check promotion ที่ได้เงินเยอะสุด
        PromotionHistory maxTurnOver = promotionHistories.stream().max(Comparator.comparing(PromotionHistory::getTurnOver)).orElse(null);
        if (maxTurnOver != null) {
            bonus = bonus.add(maxTurnOver.getBonus());
            turnOver = turnOver.add(maxTurnOver.getTurnOver());
            promotionHistoryRepository.save(maxTurnOver);
        }

        PromotionEffectiveResponse response = new PromotionEffectiveResponse();
        response.setBonus(bonus);
        response.setTurnOver(turnOver);
        return response;
    }
}
