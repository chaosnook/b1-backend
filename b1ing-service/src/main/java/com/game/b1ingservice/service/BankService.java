package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.bank.BankRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BankService {
    void insertBank(BankRequest bankRequest);
    ResponseEntity<?> getBank();
}
