package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.bankbot.BankBotAddCreditRequest;
import com.game.b1ingservice.payload.bankbot.BankBotAddCreditTrueWalletRequest;
import com.game.b1ingservice.payload.bankbot.BankBotScbWithdrawCreditRequest;
import com.game.b1ingservice.payload.bankbot.BankBotScbWithdrawCreditResponse;
import org.springframework.stereotype.Service;

@Service
public interface BankBotService {
    void addCredit(BankBotAddCreditRequest request, Long agentId);

    void addCreditTrue(BankBotAddCreditTrueWalletRequest reques, Long agentId);

    BankBotScbWithdrawCreditResponse withDrawCredit(BankBotScbWithdrawCreditRequest request, Long agentId);
}
