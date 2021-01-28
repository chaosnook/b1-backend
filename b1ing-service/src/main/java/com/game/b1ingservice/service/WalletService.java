package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.wellet.WalletRequest;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {
    void createWallet(WalletRequest req);
}
