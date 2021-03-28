package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.payload.deposithistory.*;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.postgres.jdbc.DepositHistoryJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.ProfitLossJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.dto.DepositHistoryTop20Dto;
import com.game.b1ingservice.postgres.jdbc.dto.SummaryDeposit;
import com.game.b1ingservice.postgres.jdbc.dto.SummaryWithdraw;
import com.game.b1ingservice.postgres.repository.DepositHistoryRepository;
import com.game.b1ingservice.service.DepositHistoryService;
import com.game.b1ingservice.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DepositHistoryServiceImpl implements DepositHistoryService {

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;

    @Autowired
    private DepositHistoryJdbcRepository depositHistoryJdbcRepository;

    @Autowired
    private ProfitLossJdbcRepository profitLossJdbcRepository;

    @Override
    public Page<DepositListHistorySearchResponse> findByCriteria(Specification<DepositHistory> specification, Pageable pageable, String type) {

        Page<DepositListHistorySearchResponse> searchData = depositHistoryRepository.findAll(specification, pageable).map(converter);

        if("SEVEN".equals(type)) {

            List<DepositListHistorySearchResponse> listSummary = findUserDepositSevenDay(searchData);

            Page<DepositListHistorySearchResponse> searchResponse = new PageImpl<>(listSummary, pageable, listSummary.size());
            return searchResponse;
        }

        return searchData;
    }

    @Override
    public Page<DepositSummaryHistorySearchResponse> findSummaryByCriteria(Specification<DepositHistory> specification, Pageable pageable, String type) {

        Page<DepositListHistorySearchResponse> searchData = depositHistoryRepository.findAll(specification, pageable).map(converter);

        List<DepositListHistorySearchResponse> listSummary;
        if("SEVEN".equals(type)) {

            listSummary = findUserDepositSevenDay(searchData);

        } else {

            listSummary = searchData.getContent();

        }

        return summaryHistory(listSummary, pageable);
    }

    @Override
    public List<DepositHisUserRes> searchByUser(DepositHisUserReq depositHisUserReq, String username) {
        List<DepositHistory> depositHistories = new ArrayList<>();
        if (depositHisUserReq.getStartDate() != null && depositHisUserReq.getPrevDate() != null) {
            depositHistories = depositHistoryRepository.findAllByUser_usernameAndCreatedDateBetweenOrderByCreatedDateDesc(username,
                    DateUtils.atStartOfDay(DateUtils.convertStartDate(depositHisUserReq.getPrevDate())).toInstant(),
                    DateUtils.atEndOfDay(DateUtils.convertEndDate(depositHisUserReq.getStartDate())).toInstant());
        } else if (depositHisUserReq.getStartDate() != null) {
            depositHistories = depositHistoryRepository.findAllByUser_usernameAndCreatedDateBetweenOrderByCreatedDateDesc(username,
                    DateUtils.atStartOfDay(DateUtils.convertStartDate(depositHisUserReq.getStartDate())).toInstant(),
                    DateUtils.atEndOfDay(DateUtils.convertEndDate(depositHisUserReq.getStartDate())).toInstant());
        }
        return depositHistories.stream().map(converterUser).collect(Collectors.toList());
    }

    @Override
    public List<DepositHistoryTop20Resp> findListByUsername(String username) {

       List<DepositHistoryTop20Dto> depositDtos = depositHistoryJdbcRepository.findTop20DepositHistory(username);

       List<DepositHistoryTop20Resp> result = new ArrayList<>();
       for(DepositHistoryTop20Dto depositDto: depositDtos) {
           DepositHistoryTop20Resp deposit = new DepositHistoryTop20Resp();
           deposit.setAmount(depositDto.getAmount());
           deposit.setBonus(depositDto.getBonus());
           deposit.setBeforeAmount(depositDto.getBeforeAmount());
           deposit.setAddCredit(depositDto.getAddCredit());
           deposit.setAfterAmount(depositDto.getAfterAmount());

           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
           Date date = new Date();
           date.setTime(depositDto.getCreatedDate().getTime());
           deposit.setCreatedDate(sdf.format(date));

           deposit.setReason(depositDto.getReason());
           result.add(deposit);
       }

        return result;
    }

    @Override
    public List<DepositHistoryByUserIdResp> findListByUserId(Long userId) {
        List<DepositHistory> list = depositHistoryRepository.findTop10ByUser_IdOrderByCreatedDateDesc(userId);
        List<DepositHistoryByUserIdResp> result = new ArrayList<>();
        if(!list.isEmpty()) {
            for(DepositHistory depositDto : list) {
                DepositHistoryByUserIdResp deposit = new DepositHistoryByUserIdResp();
                if(null == depositDto.getBank()) {
                    deposit.setBankName(null);
                    deposit.setBankCode(null);
                } else {
                    deposit.setBankName(depositDto.getBank().getBankName());
                    deposit.setBankCode(depositDto.getBank().getBankCode());
                }
                deposit.setAmount(depositDto.getAmount().toString());
                deposit.setBonus(depositDto.getBonusAmount().toString());
                deposit.setAddCredit(depositDto.getAmount().add(depositDto.getBonusAmount()).toString());
                deposit.setBeforeAmount(depositDto.getBeforeAmount().toString());
                deposit.setAfterAmount(depositDto.getAfterAmount().toString());

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
                Date date = Date.from(depositDto.getCreatedDate());
                deposit.setCreatedDate(sdf.format(date));

                deposit.setReason(depositDto.getReason());

                result.add(deposit);
            }
        }

        return result;
    }

    @Override
    public ProfitAndLossResp findProfitAndLoss(ProfitAndLossRequest req) {

        ProfitAndLossResp resp = new ProfitAndLossResp();

        List<SummaryDeposit> listDeposit = profitLossJdbcRepository.sumDeposit(req);
        if(!listDeposit.isEmpty()) {
            SummaryDeposit deposit = listDeposit.get(0);

            if(null == deposit.getDeposit()) {
                resp.setDeposit(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
            } else {
                resp.setDeposit(deposit.getDeposit());
            }

            if(null == deposit.getBonus()) {
                resp.setBonus("0.00(0.00%)");
            } else {
                String sum = (deposit.getBonus().divide(resp.getDeposit(),2 , RoundingMode.HALF_UP)).multiply(new BigDecimal(100)).toString();
                String result = deposit.getBonus().setScale(2, RoundingMode.HALF_UP) + "(" + sum +  "%)" ;
                resp.setBonus(result);
            }

            if(null == deposit.getDepositBonus()) {
                resp.setDepositBonus(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
            } else {
                resp.setDepositBonus(deposit.getDepositBonus());
            }
        } else {
            resp.setDeposit(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
            resp.setBonus("0.00(0.00%)");
            resp.setDepositBonus(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
        }


        List<SummaryWithdraw> listWithdraw = profitLossJdbcRepository.sumWithdraw(req);
        if(!listWithdraw.isEmpty()) {
            SummaryWithdraw withdraw = listWithdraw.get(0);
            if(null == withdraw.getWithdraw()) {
                resp.setWithdraw(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
            } else {
                resp.setWithdraw(withdraw.getWithdraw());
            }

            BigDecimal sum = resp.getDeposit().subtract(resp.getWithdraw()).setScale(2, RoundingMode.HALF_UP);
            resp.setProfitAndLoss(sum);
        } else {
            resp.setWithdraw(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
            BigDecimal sum = resp.getDeposit().subtract(resp.getWithdraw()).setScale(2, RoundingMode.HALF_UP);
            resp.setProfitAndLoss(sum);
        }

        return resp;

    }

    private Page<DepositSummaryHistorySearchResponse> summaryHistory(List<DepositListHistorySearchResponse> searchData, Pageable pageable) {

        Map<String, DepositSummaryHistorySearchResponse> map = new HashMap<>();
        for(DepositListHistorySearchResponse depositList: searchData) {

            DepositSummaryHistorySearchResponse summary = new DepositSummaryHistorySearchResponse();

            if(!map.containsKey(depositList.getBankName())) {

                summary.setBankName(depositList.getBankName());
                summary.setBankCode(depositList.getBankCode());
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
                summary.setBankCode(value.getBankCode());

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

    private List<DepositListHistorySearchResponse> findUserDepositSevenDay(Page<DepositListHistorySearchResponse> searchData) {

        Map<String, SevenDay> map = new HashMap<>();

        List<DepositListHistorySearchResponse> listData = searchData.getContent().stream()
                .sorted(Comparator.comparing(DepositListHistorySearchResponse::getCreatedDate)).collect(Collectors.toList());

        for(DepositListHistorySearchResponse depositHistory : listData) {

            String date = depositHistory.getCreatedDate().substring(0,2);
            if(!map.containsKey(depositHistory.getUsername())) {

                SevenDay sevenDay = new SevenDay();
                sevenDay.setDay(date);
                sevenDay.setCount(1);
                map.put(depositHistory.getUsername(), sevenDay);

            } else {

                SevenDay sevenDay = map.get(depositHistory.getUsername());
                if(!sevenDay.getDay().equals(date)) {
                    SevenDay sevenDayNew = new SevenDay();
                    sevenDayNew.setDay(date);
                    sevenDayNew.setCount(sevenDay.getCount() + 1);
                    map.replace(depositHistory.getUsername(), sevenDayNew);
                }
            }
        }

        List<String> listUser = new ArrayList<>();
        for(Map.Entry<String, SevenDay> entry : map.entrySet()) {
            if(entry.getValue().getCount() == 7){
                listUser.add(entry.getKey());
            }
        }

        List<DepositListHistorySearchResponse> listSummary = new ArrayList<>();
        for(String username : listUser) {
            List<DepositListHistorySearchResponse> list = searchData.getContent().stream()
                    .filter(obj -> obj.getUsername().equals(username)).collect(Collectors.toList());

            for(DepositListHistorySearchResponse deposit : list) {
                listSummary.add(deposit);
            }
        }

        return listSummary;
    }

    Function<DepositHistory, DepositListHistorySearchResponse> converter = depositHistory -> {
        DepositListHistorySearchResponse searchResponse = new DepositListHistorySearchResponse();
        searchResponse.setId(depositHistory.getId());
        if(null == depositHistory.getBank()) {
            searchResponse.setBankName(null);
            searchResponse.setBankCode(null);
        } else {
            searchResponse.setBankName(depositHistory.getBank().getBankName());
            searchResponse.setBankCode(depositHistory.getBank().getBankCode());
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
