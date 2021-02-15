package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.bank.BankRequest;
import com.game.b1ingservice.payload.bankdeposit.UserBankDepositResponse;
import com.game.b1ingservice.payload.bankdeposit.UserTrueWalletResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BankService {
    void insertBank(BankRequest bankRequest, UserPrincipal principal);
    ResponseEntity<?> getBank();
    void updateBank(Long id, BankRequest bankRequest);
    void deleteBank(Long id);

    UserBankDepositResponse getUserBankDeposit(String username , String prefix);

    UserTrueWalletResponse getUserTrueWallet(String username, String prefix);
}
