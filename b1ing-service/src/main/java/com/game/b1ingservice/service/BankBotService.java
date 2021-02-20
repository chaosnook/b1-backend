package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.bankbot.BankBotAddCreditRequest;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditTrueWalletRequest;
import org.springframework.stereotype.Service;

@Service
public interface BankBotService {
    void addCredit(BankBotAddCreditRequest request);
    void addCreditTrue(BankBotAddCreditTrueWalletRequest request);
//    void withDrawCredit();
}
