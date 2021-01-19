package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.truewallet.TrueWalletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface TrueWalletService {
    void insertTrueWallet(TrueWalletRequest req);
    ResponseEntity<?> getTrueWallet();
    void updateTrueWallet(Long id, TrueWalletRequest req);
    void deleteTrueWallet(Long id);
}
