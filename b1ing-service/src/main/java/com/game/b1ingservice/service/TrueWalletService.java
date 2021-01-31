package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.truewallet.TrueWalletRequest;
import com.game.b1ingservice.payload.truewallet.TrueWalletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrueWalletService {
    void insertTrueWallet(TrueWalletRequest req);
    List<TrueWalletResponse> getTrueWallet();
    void updateTrueWallet(Long id, TrueWalletRequest req);
    void deleteTrueWallet(Long id);
}
