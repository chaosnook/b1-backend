package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.deposithistory.DepositHistorySearchResponse;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.postgres.repository.DepositHistoryRepository;
import com.game.b1ingservice.service.DepositHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DepositHistoryServiceImpl implements DepositHistoryService {

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;

    @Override
    public Page<DepositHistorySearchResponse> findByCriteria(Specification<DepositHistory> specification, Pageable pageable) {
        return depositHistoryRepository.findAll(specification, pageable).map(converter);
    }

    Function<DepositHistory, DepositHistorySearchResponse> converter = depositHistory -> {
        DepositHistorySearchResponse searchResponse = new DepositHistorySearchResponse();
        searchResponse.setId(depositHistory.getId());
        searchResponse.setBank(depositHistory.getBank());
        searchResponse.setUser(depositHistory.getUser());
        searchResponse.setAmount(depositHistory.getAmount());
        searchResponse.setBeforeAmount(depositHistory.getBeforeAmount());
        searchResponse.setAfterAmount(depositHistory.getAfterAmount());
        searchResponse.setType(depositHistory.getType());
        searchResponse.setIsAuto(depositHistory.getIsAuto());
        searchResponse.setReason(depositHistory.getReason());
        searchResponse.setAdmin(depositHistory.getAdmin());

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
