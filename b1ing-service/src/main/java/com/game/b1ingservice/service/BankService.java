package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.bank.BankRequest;
import com.game.b1ingservice.payload.bank.BankResponse;
import com.game.b1ingservice.payload.bankdeposit.UserBankDepositResponse;
import com.game.b1ingservice.payload.bankdeposit.UserTrueWalletResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BankService {
    void insertBank(BankRequest bankRequest, UserPrincipal principal);

    List<BankResponse> getBank(Long agentId);

    void updateBank(Long id, BankRequest bankRequest, Long agentId);

    void deleteBank(Long id, Long agentId);

    UserBankDepositResponse getUserBankDeposit(String username, Long agentId);

    UserTrueWalletResponse getUserTrueWallet(String username, Long agentId);
}
