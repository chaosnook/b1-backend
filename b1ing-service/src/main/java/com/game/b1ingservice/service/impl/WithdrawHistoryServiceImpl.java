package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.withdraw.WithdrawHisUserReq;
import com.game.b1ingservice.payload.withdraw.WithdrawHisUserRes;
import com.game.b1ingservice.payload.withdrawhistory.WithdrawHistoryByUserIdResp;
import com.game.b1ingservice.payload.withdrawhistory.WithdrawListHistorySearchResponse;
import com.game.b1ingservice.payload.withdrawhistory.WithdrawSummaryHistorySearchResponse;
import com.game.b1ingservice.postgres.entity.WithdrawHistory;
import com.game.b1ingservice.postgres.repository.WithdrawHistoryRepository;
import com.game.b1ingservice.service.WithdrawHistoryService;
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
public class WithdrawHistoryServiceImpl implements WithdrawHistoryService {

    @Autowired
    private WithdrawHistoryRepository withdrawHistoryRepository;

    @Override
    public WithdrawHistory saveHistory(WithdrawHistory withdrawHistory) {
        return withdrawHistoryRepository.save(withdrawHistory);
    }

    @Override
    public List<WithdrawHisUserRes> searchByUser(WithdrawHisUserReq withDrawRequest, String username) {
        List<WithdrawHistory> depositHistories = new ArrayList<>();
        if (withDrawRequest.getStartDate() != null && withDrawRequest.getPrevDate() != null) {
            depositHistories = withdrawHistoryRepository.findAllByUser_usernameAndCreatedDateBetweenOrderByCreatedDateDesc(username,
                    DateUtils.atStartOfDay(DateUtils.convertStartDate(withDrawRequest.getPrevDate())).toInstant(),
                    DateUtils.atEndOfDay(DateUtils.convertEndDate(withDrawRequest.getStartDate())).toInstant());
        } else if (withDrawRequest.getStartDate() != null) {
            depositHistories = withdrawHistoryRepository.findAllByUser_usernameAndCreatedDateBetweenOrderByCreatedDateDesc(username,
                    DateUtils.atStartOfDay(DateUtils.convertStartDate(withDrawRequest.getStartDate())).toInstant(),
                    DateUtils.atEndOfDay(DateUtils.convertEndDate(withDrawRequest.getStartDate())).toInstant());
        }
        return depositHistories.stream().map(converterUser).collect(Collectors.toList());
    }

    @Override
    public List<WithdrawHistoryByUserIdResp> findListByUserId(Long userId) {
        List<WithdrawHistory> list = withdrawHistoryRepository.findTop10ByUser_IdOrderByCreatedDateDesc(userId);
        List<WithdrawHistoryByUserIdResp> result = new ArrayList<>();
        if(!list.isEmpty()) {
            for(WithdrawHistory withdrawDto : list) {
                WithdrawHistoryByUserIdResp withdraw = new WithdrawHistoryByUserIdResp();
                if(null == withdrawDto.getBank()) {
                    withdraw.setBankName(null);
                    withdraw.setBankCode(null);
                } else {
                    withdraw.setBankName(withdrawDto.getBank().getBankName());
                    withdraw.setBankCode(withdrawDto.getBank().getBankCode());
                }
                withdraw.setType(withdrawDto.getType());
                withdraw.setAmount(withdrawDto.getAmount().toString());
                withdraw.setStatus(withdrawDto.getStatus());

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
                Date date = Date.from(withdrawDto.getCreatedDate());
                withdraw.setCreatedDate(sdf.format(date));

                withdraw.setReason(withdrawDto.getReason());

                result.add(withdraw);
            }
        }

        return result;
    }

    Function<WithdrawHistory, WithdrawHisUserRes> converterUser = withdrawHistory -> {
        WithdrawHisUserRes witHis = new WithdrawHisUserRes();
        witHis.setReason(withdrawHistory.getReason());
        witHis.setCreatedDate(withdrawHistory.getCreatedDate());
        witHis.setStatus(withdrawHistory.getStatus());
        witHis.setValue(withdrawHistory.getAmount());
        return witHis;
    };

    @Override
    public Page<WithdrawListHistorySearchResponse> findByCriteria(Specification<WithdrawHistory> specification, Pageable pageable, String type) {

        Page<WithdrawListHistorySearchResponse> searchData = withdrawHistoryRepository.findAll(specification, pageable).map(converter);
        return searchData;
    }

    @Override
    public Page<WithdrawSummaryHistorySearchResponse> findSummaryByCriteria(Specification<WithdrawHistory> specification, Pageable pageable, String type) {

        Page<WithdrawSummaryHistorySearchResponse> searchData = withdrawHistoryRepository.findAll(specification, pageable).map(converter2);
        return summaryHistory(searchData.getContent(), pageable);
    }

    private Page<WithdrawSummaryHistorySearchResponse> summaryHistory(List<WithdrawSummaryHistorySearchResponse> searchData, Pageable pageable) {

        Map<String, WithdrawSummaryHistorySearchResponse> map = new HashMap<>();
        for(WithdrawSummaryHistorySearchResponse withdraw: searchData) {

            WithdrawSummaryHistorySearchResponse summary = new WithdrawSummaryHistorySearchResponse();

            if(!map.containsKey(withdraw.getBankName())) {

                summary.setBankName(withdraw.getBankName());
                summary.setCountTask(1);
                summary.setTotalWithdraw(withdraw.getTotalWithdraw());
                summary.setBankGroup(withdraw.getBankGroup());
                summary.setBankCode(withdraw.getBankCode());

                map.put(withdraw.getBankName(), summary);

            } else {

                WithdrawSummaryHistorySearchResponse value = map.get(withdraw.getBankName());
                summary.setBankName(value.getBankName());
                summary.setCountTask(value.getCountTask() + 1);
                summary.setTotalWithdraw(value.getTotalWithdraw().add(withdraw.getTotalWithdraw()));
                summary.setBankGroup(value.getBankGroup());
                summary.setBankCode(value.getBankCode());

                map.replace(withdraw.getBankName(), summary);
            }
        }

        List<WithdrawSummaryHistorySearchResponse> listSummary = new ArrayList<>();
        for (Map.Entry<String, WithdrawSummaryHistorySearchResponse> entry : map.entrySet()) {
            listSummary.add(entry.getValue());
        }

        Page<WithdrawSummaryHistorySearchResponse> searchResponse = new PageImpl<>(listSummary, pageable, listSummary.size());
        return searchResponse;
    }

    Function<WithdrawHistory, WithdrawListHistorySearchResponse> converter = withdrawHistory -> {
        WithdrawListHistorySearchResponse searchResponse = new WithdrawListHistorySearchResponse();
        searchResponse.setId(withdrawHistory.getId());
        if(null == withdrawHistory.getBank()) {
            searchResponse.setBankName(null);
            searchResponse.setBankCode(null);
        } else {
            searchResponse.setBankName(withdrawHistory.getBank().getBankName());
            searchResponse.setBankCode(withdrawHistory.getBank().getBankCode());
        }
        if(null == withdrawHistory.getUser()) {
            searchResponse.setUsername(null);
        } else {
            searchResponse.setUsername(withdrawHistory.getUser().getUsername());
        }
        searchResponse.setAmount(withdrawHistory.getAmount());
        searchResponse.setBeforeAmount(withdrawHistory.getBeforeAmount());
        searchResponse.setAfterAmount(withdrawHistory.getAfterAmount());
        searchResponse.setType(withdrawHistory.getType());
        searchResponse.setStatus(withdrawHistory.getStatus());
        searchResponse.setIsAuto(withdrawHistory.getIsAuto());
        searchResponse.setReason(withdrawHistory.getReason());
        if(null == withdrawHistory.getAdmin()) {
            searchResponse.setAdmin(null);
        } else {
            searchResponse.setAdmin(withdrawHistory.getAdmin().getUsername());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");

        searchResponse.setCreatedDate(sdf.format(Date.from(withdrawHistory.getCreatedDate())));
        searchResponse.setUpdatedDate(sdf.format(Date.from(withdrawHistory.getUpdatedDate())));
        searchResponse.setCreatedBy(withdrawHistory.getAudit().getCreatedBy());
        searchResponse.setUpdatedBy(withdrawHistory.getAudit().getUpdatedBy());
        searchResponse.setDeleteFlag(withdrawHistory.getDeleteFlag());
        searchResponse.setVersion(withdrawHistory.getVersion());

        Map<String, Object> configMap = new HashMap<>();
        return searchResponse;
    };

    Function<WithdrawHistory, WithdrawSummaryHistorySearchResponse> converter2 = withdrawHistory -> {
        WithdrawSummaryHistorySearchResponse searchResponse = new WithdrawSummaryHistorySearchResponse();
        if(null == withdrawHistory.getBank()) {
            searchResponse.setBankName(null);
            searchResponse.setBankGroup(null);
            searchResponse.setBankCode(null);
        } else {
            searchResponse.setBankName(withdrawHistory.getBank().getBankName());
            searchResponse.setBankGroup(String.valueOf(withdrawHistory.getBank().getBankGroup()));
            searchResponse.setBankCode(withdrawHistory.getBank().getBankCode());
        }

        searchResponse.setTotalWithdraw(withdrawHistory.getAmount());

        return searchResponse;
    };


}
