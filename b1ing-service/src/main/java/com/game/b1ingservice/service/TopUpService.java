package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.topup.TopUpResponse;
import com.game.b1ingservice.payload.topup.TopupRequest;
import org.springframework.stereotype.Service;

@Service
public interface TopUpService {
    TopUpResponse topUpReport(TopupRequest topupRequest, UserPrincipal principal);
}
