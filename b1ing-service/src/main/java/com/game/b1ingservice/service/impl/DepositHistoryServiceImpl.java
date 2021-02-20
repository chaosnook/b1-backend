package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.deposithistory.DepositHisUserReq;
import com.game.b1ingservice.payload.deposithistory.DepositHisUserRes;
import com.game.b1ingservice.payload.deposithistory.DepositListHistorySearchResponse;
import com.game.b1ingservice.payload.deposithistory.DepositSummaryHistorySearchResponse;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.postgres.repository.DepositHistoryRepository;
import com.game.b1ingservice.service.DepositHistoryService;
import com.game.b1ingservice.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DepositHistoryServiceImpl implements DepositHistoryService {

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;

    @Override
    public Page<DepositListHistorySearchResponse> findByCriteria(Specification<DepositHistory> specification, Pageable pageable, String type) {

        Page<DepositListHistorySearchResponse> searchResponse = depositHistoryRepository.findAll(specification, pageable).map(converter);

        if("SEVEN".equals(type)) {

        } else {

        }

        return searchResponse;
    }

    @Override
    public Page<DepositSummaryHistorySearchResponse> findSummaryByCriteria(Specification<DepositHistory> specification, Pageable pageable, String type) {

        Page<DepositListHistorySearchResponse> searchData = depositHistoryRepository.findAll(specification, pageable).map(converter);

        List<DepositListHistorySearchResponse> listSummary = new ArrayList<>();
        if("SEVEN".equals(type)) {

        } else {

        }

        return summaryHistory(searchData.getContent(), pageable);
    }

    @Override
    public List<DepositHisUserRes> searchByUser(DepositHisUserReq depositHisUserReq, String username) {
        List<DepositHistory> depositHistories = new ArrayList<>();
        if (depositHisUserReq.getStartDate() != null && depositHisUserReq.getEndDate() != null) {
            depositHistories = depositHistoryRepository.findAllByUser_usernameAndCreatedDateBetweenOrderByCreatedDateDesc(username,
                    DateUtils.atStartOfDay(DateUtils.convertStartDate(depositHisUserReq.getStartDate())).toInstant(),
                    DateUtils.atEndOfDay(DateUtils.convertEndDate(depositHisUserReq.getEndDate())).toInstant());
        } else if (depositHisUserReq.getStartDate() != null) {
            depositHistories = depositHistoryRepository.findAllByUser_usernameAndCreatedDateBetweenOrderByCreatedDateDesc(username,
                    DateUtils.atStartOfDay(DateUtils.convertStartDate(depositHisUserReq.getStartDate())).toInstant(),
                    DateUtils.atEndOfDay(DateUtils.convertEndDate(depositHisUserReq.getStartDate())).toInstant());
        }
        return depositHistories.stream().map(converterUser).collect(Collectors.toList());
    }

    private Page<DepositSummaryHistorySearchResponse> summaryHistory(List<DepositListHistorySearchResponse> searchData, Pageable pageable) {

        Map<String, DepositSummaryHistorySearchResponse> map = new HashMap<>();
        for(DepositListHistorySearchResponse depositList: searchData) {

            DepositSummaryHistorySearchResponse summary = new DepositSummaryHistorySearchResponse();

            if(!map.containsKey(depositList.getBankName())) {

                summary.setBankName(depositList.getBankName());
                summary.setCountTask(1);
                summary.setTotalDeposit(depositList.getAmount());
                summary.setTotalBonus(depositList.getBonus());

                map.put(depositList.getBankName(), summary);

            } else {

                DepositSummaryHistorySearchResponse value = map.get(depositList.getBankName());
                summary.setBankName(value.getBankName());
                summary.setCountTask(value.getCountTask() + 1);
                summary.setTotalDeposit(value.getTotalDeposit().add(depositList.getAmount()));
                summary.setTotalBonus(value.getTotalBonus().add(depositList.getBonus()));

                map.replace(depositList.getBankName(), summary);
            }
        }

        List<DepositSummaryHistorySearchResponse> listSummary = new ArrayList<>();
        for (Map.Entry<String, DepositSummaryHistorySearchResponse> entry : map.entrySet()) {
            listSummary.add(entry.getValue());
        }

        Page<DepositSummaryHistorySearchResponse> searchResponse = new PageImpl<>(listSummary, pageable, listSummary.size());
        return searchResponse;
    }

    Function<DepositHistory, DepositListHistorySearchResponse> converter = depositHistory -> {
        DepositListHistorySearchResponse searchResponse = new DepositListHistorySearchResponse();
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");

        searchResponse.setBonus(depositHistory.getBonusAmount());
        searchResponse.setCreatedDate(sdf.format(Date.from(depositHistory.getCreatedDate())));
        searchResponse.setUpdatedDate(sdf.format(Date.from(depositHistory.getUpdatedDate())));
        searchResponse.setCreatedBy(depositHistory.getAudit().getCreatedBy());
        searchResponse.setUpdatedBy(depositHistory.getAudit().getUpdatedBy());
        searchResponse.setDeleteFlag(depositHistory.getDeleteFlag());
        searchResponse.setVersion(depositHistory.getVersion());

        Map<String, Object> configMap = new HashMap<>();
        return searchResponse;
    };


    Function<DepositHistory, DepositHisUserRes> converterUser = depositHistory -> {
        DepositHisUserRes depHis = new DepositHisUserRes();
        depHis.setReason(depositHistory.getReason());
        depHis.setCreatedDate(depositHistory.getCreatedDate());
        depHis.setStatus(depositHistory.getStatus());
        depHis.setValue(depositHistory.getAmount());
        depHis.setBonus(depositHistory.getBonusAmount());
        return depHis;
    };
}
