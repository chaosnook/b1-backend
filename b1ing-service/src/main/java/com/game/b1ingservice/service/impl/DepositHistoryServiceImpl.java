package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.deposithistory.DepositHistorySearchResponse;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.postgres.repository.DepositHistoryRepository;
import com.game.b1ingservice.service.DepositHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class DepositHistoryServiceImpl implements DepositHistoryService {

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;

    @Override
    public Page<DepositHistorySearchResponse> findByCriteria(Specification<DepositHistory> specification, Pageable pageable, String type) {

        Page<DepositHistorySearchResponse> searchResponse = depositHistoryRepository.findAll(specification, pageable).map(converter);

        if("sevenDay".equals(type)) {

//            return
        }

        return searchResponse;
    }

    Function<DepositHistory, DepositHistorySearchResponse> converter = depositHistory -> {
        DepositHistorySearchResponse searchResponse = new DepositHistorySearchResponse();
        searchResponse.setId(depositHistory.getId());
        if(null == depositHistory.getBank()) {
            searchResponse.setBankName(null);
        } else {
            searchResponse.setBankName(depositHistory.getBank().getBankName());
        }
        if(null == depositHistory.getUser()) {
            searchResponse.setUsername(null);
        } else {
            searchResponse.setUsername(depositHistory.getUser().getUsername());
        }
        searchResponse.setAmount(depositHistory.getAmount());
        searchResponse.setBeforeAmount(depositHistory.getBeforeAmount());
        searchResponse.setAfterAmount(depositHistory.getAfterAmount());
        searchResponse.setType(depositHistory.getType());
        searchResponse.setStatus(depositHistory.getStatus());
        searchResponse.setIsAuto(depositHistory.getIsAuto());
        searchResponse.setReason(depositHistory.getReason());
        if(null == depositHistory.getAdmin()) {
            searchResponse.setAdmin(null);
        } else {
            searchResponse.setAdmin(depositHistory.getAdmin().getUsername());
        }
        searchResponse.setCreatedDate(depositHistory.getCreatedDate());
        searchResponse.setUpdatedDate(depositHistory.getUpdatedDate());
        searchResponse.setCreatedBy(depositHistory.getAudit().getCreatedBy());
        searchResponse.setUpdatedBy(depositHistory.getAudit().getUpdatedBy());
        searchResponse.setDeleteFlag(depositHistory.getDeleteFlag());
        searchResponse.setVersion(depositHistory.getVersion());

        Map<String, Object> configMap = new HashMap<>();
        return searchResponse;
    };
}
