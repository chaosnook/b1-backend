package com.game.b1ingservice.service;

import org.springframework.http.ResponseEntity;

public interface MasterBankService {
    ResponseEntity<?> getMasterBankDeposit();
    ResponseEntity<?> getMasterBankWithdraw();
}
