package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.wellet.WalletRequest;
import com.game.b1ingservice.postgres.entity.WebUser;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {
    void createWallet(WalletRequest req, WebUser user);
}
