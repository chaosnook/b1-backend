package com.game.b1ingservice.schedule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditTrueWalletRequest;
import com.game.b1ingservice.payload.bankbot.BankBotTrueTransactionResponse;
import com.game.b1ingservice.postgres.entity.TrueWallet;
import com.game.b1ingservice.postgres.repository.TrueWalletRepository;
import com.game.b1ingservice.service.BankBotService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TrueWalletTask {

    @Autowired
    private TrueWalletRepository trueWalletRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OkHttpClient client;

    @Autowired
    private BankBotService bankBotService;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");

//    @Scheduled(cron = "0,30 * * * * *")
    @SchedulerLock(name = "scheduleFixedRateTask",
        lockAtLeastForString = "PT30S", lockAtMostForString = "PT5M")
    public void scheduleFixedRateTask() {
        try {
            TimeUnit.SECONDS.sleep(new Random().nextInt(5));

            List<TrueWallet> lists = trueWalletRepository.findAll();
            for (TrueWallet trueWallet : lists) {
                List<BankBotTrueTransactionResponse> transactionList = fetchTrueTransaction(trueWallet);
                for(BankBotTrueTransactionResponse transaction : transactionList){
                    BankBotAddCreditTrueWalletRequest request = new BankBotAddCreditTrueWalletRequest();
                    request.setBotType("TRUE");
                    request.setTransactionId("transactionId");
                    request.setBotIp(trueWallet.getBotIp());
                    request.setTrueTranID(transaction.getTranID());
                    request.setAmount(BigDecimal.valueOf(transaction.getAmount()));
                    try {
                        Date d = sdf.parse(transaction.getTranDate());
                        request.setTransactionDate(d.toInstant());
                    }catch (Exception e){

                    }
                    request.setType("Deposit");
                    request.setMobile(transaction.getMobile());
                    request.setTransactionId(DigestUtils.sha1Hex(request.getTrueTranID()+request.getMobile()));
                    bankBotService.addCreditTrue(request);
                }
            }
        }catch (InterruptedException e){
            log.error(e.getLocalizedMessage());
        }
    }

    private List<BankBotTrueTransactionResponse> fetchTrueTransaction(TrueWallet trueWallet) {
        try {

            Request request = new Request.Builder()
                    .url(String.format("http://%s/truewallet/api", trueWallet.getBotIp()))
                    .method("GET", null)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            return objectMapper.readValue(response.body().string(), new TypeReference<List<BankBotTrueTransactionResponse>>() {
            });

        } catch (Exception e) {
            log.error("fetchTrueTransaction error : ",e);
        }
        return new ArrayList<>();
    }
}


