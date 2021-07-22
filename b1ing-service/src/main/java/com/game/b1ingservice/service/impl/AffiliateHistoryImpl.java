package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.affiliate.AffHistoryRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.jdbc.SearchAffiliateHistoryJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.dto.SearchAffiHistoryDTO;
import com.game.b1ingservice.service.AffiliateHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class AffiliateHistoryImpl implements AffiliateHistoryService {

    @Autowired
    private SearchAffiliateHistoryJdbcRepository searchAffiliateHistoryJdbcRepository;


    @Override
    public List<SearchAffiHistoryDTO> affiHistory(AffHistoryRequest affHistoryRequest, UserPrincipal principal) {

        List<SearchAffiHistoryDTO> listAffi = searchAffiliateHistoryJdbcRepository.affiHistory(affHistoryRequest, principal);

        List<SearchAffiHistoryDTO> result = new ArrayList<>();
        for (SearchAffiHistoryDTO searchAffiHistoryDTO : listAffi) {
            SearchAffiHistoryDTO search = new SearchAffiHistoryDTO();
            search.setUsername(searchAffiHistoryDTO.getUsername());
            search.setAmount(searchAffiHistoryDTO.getAmount());
            result.add(search);
        }

        return result;

    }

}


