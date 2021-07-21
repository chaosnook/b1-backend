package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.userinfo.UserWalletResponse;
import com.game.b1ingservice.payload.wallet.WalletRequest;
import com.game.b1ingservice.postgres.entity.WebUser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface WalletService {
    void createWallet(WalletRequest req, WebUser user);

    UserWalletResponse getUserWallet(String username, Long agentId);

    BigDecimal updateCurrentWallet(WebUser webUser);

    void minusTurnOver(Long id, BigDecimal afterAmount);
}
