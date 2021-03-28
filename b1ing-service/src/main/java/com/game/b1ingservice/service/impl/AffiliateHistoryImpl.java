package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.affiliate.AffHistoryRequest;
import com.game.b1ingservice.payload.affiliate.AffHistoryResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.jdbc.SearchAffiliateHistoryJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.dto.SearchAffiHistoryDTO;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.service.AffiliateHistoryService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class AffiliateHistoryImpl implements AffiliateHistoryService {
    @Autowired
    SearchAffiliateHistoryJdbcRepository searchAffiliateHistoryJdbcRepository;

    @Override
    public AffHistoryResponse affiHistory(AffHistoryRequest affHistoryRequest, UserPrincipal principal) {

        AffHistoryResponse resObj = new AffHistoryResponse();

        List<SearchAffiHistoryDTO> listAffi = searchAffiliateHistoryJdbcRepository.affiHistory(affHistoryRequest, principal);


        for(SearchAffiHistoryDTO searchAffiHistoryDTO : listAffi) {
            resObj.setUsername(searchAffiHistoryDTO.getUsername());
            resObj.setAmount(searchAffiHistoryDTO.getAmount());
        }

        return resObj ;

        }

    }


