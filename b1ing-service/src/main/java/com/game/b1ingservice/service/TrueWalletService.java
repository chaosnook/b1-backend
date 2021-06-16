package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.truewallet.TrueWalletRequest;
import com.game.b1ingservice.payload.truewallet.TrueWalletResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrueWalletService {
    void insertTrueWallet(TrueWalletRequest req, UserPrincipal principal);

    List<TrueWalletResponse> getTrueWallet(UserPrincipal principal);

    void updateTrueWallet(Long id, TrueWalletRequest req, UserPrincipal principal);

    void deleteTrueWallet(Long id, UserPrincipal principal);
}
