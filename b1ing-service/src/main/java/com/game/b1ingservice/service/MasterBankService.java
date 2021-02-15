package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.MasterBank.MasterBankResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MasterBankService {
    ResponseEntity<?> getMasterBankDeposit();
    ResponseEntity<?> getMasterBankWithdraw();

    List<MasterBankResponse> getMasterBankUser();
}
