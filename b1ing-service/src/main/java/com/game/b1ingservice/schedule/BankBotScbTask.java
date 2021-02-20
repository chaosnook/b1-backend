package com.game.b1ingservice.schedule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditRequest;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditTrueWalletRequest;
import com.game.b1ingservice.payload.bankbot.BankBotScbTransactionResponse;
import com.game.b1ingservice.payload.bankbot.BankBotTrueTransactionResponse;
import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.repository.BankRepository;
import com.game.b1ingservice.service.BankBotService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class BankBotScbTask {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OkHttpClient client;

    @Autowired
    private BankBotService bankBotService;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Scheduled(cron = "0,21,39,59 * * * * *")
    public void scheduleFixedRateTask() {
        List<Bank> lists = bankRepository.findAll();
        for (Bank bank : lists) {
            if (!bank.getBotIp().startsWith("100.101.1")) {
                List<BankBotScbTransactionResponse> transactionList = fetchScbTransaction(bank);
                for (BankBotScbTransactionResponse transaction : transactionList) {
                    BankBotAddCreditRequest request = new BankBotAddCreditRequest();
                    request.setBotType("SCB");
                    request.setTransactionId("transactionId");
                    request.setBotIp(bank.getBotIp());
                    request.setAccountNo(transaction.getAccountNo());
                    request.setAmount(BigDecimal.valueOf(transaction.getTxnAmount()));
                    try {
                        Date d = sdf.parse(transaction.getTxnDateTime().replace(".000+07:00", ""));
                        request.setTransactionDate(d.toInstant());
                    } catch (Exception e) {

                    }
                    request.setType("Deposit");
                    request.setRemark(transaction.getTxnRemark());
                    request.setTransactionId(DigestUtils.sha1Hex(request.getTransactionDate().toString() + (request.getRemark())));
                    request = extractAccount(request);
                    log.info(request.toString());
                    bankBotService.addCredit(request);
                }
            }
        }
    }

    private List<BankBotScbTransactionResponse> fetchScbTransaction(Bank bank) {
        try {

            Request request = new Request.Builder()
                    .url(String.format("http://%s/scb/api", bank.getBotIp()))
                    .method("GET", null)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            return objectMapper.readValue(response.body().string(), new TypeReference<List<BankBotScbTransactionResponse>>() {
            });

        } catch (Exception e) {
            log.error("fetchTrueTransaction error : ", e);
        }
        return new ArrayList<>();
    }

    private BankBotAddCreditRequest extractAccount(BankBotAddCreditRequest request) {
        String[] splited = request.getRemark().split(" ");
        if (request.getRemark().contains(" SCB ")) {
            request.setAccountNo(splited[2].replace("x", ""));
            request.setFirstName(splited[4]);
            request.setLastName(splited[5]);
        } else {
            request.setAccountNo(splited[2].replace("/X", ""));
        }
        return request;
    }
}


