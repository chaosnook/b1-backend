package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.affiliate.AffHistoryRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;

import com.game.b1ingservice.postgres.jdbc.dto.SearchAffiHistoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AffiliateHistoryService {
    List<SearchAffiHistoryDTO> affiHistory(AffHistoryRequest affHistoryRequest, UserPrincipal principal);

}
