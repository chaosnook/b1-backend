package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.affiliate.AffHistoryRequest;
import com.game.b1ingservice.payload.affiliate.AffHistoryResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;

import org.springframework.stereotype.Service;

@Service
public interface AffiliateHistoryService {
    AffHistoryResponse affiHistory(AffHistoryRequest affHistoryRequest, UserPrincipal principal);

}
