package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.amb.AmbResponse;
import com.game.b1ingservice.payload.amb.DepositReq;
import com.game.b1ingservice.payload.amb.DepositRes;
import com.game.b1ingservice.payload.bankbot.BankBotScbWithdrawCreditRequest;
import com.game.b1ingservice.payload.bankbot.BankBotScbWithdrawCreditResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.deposithistory.*;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.jdbc.DepositHistoryJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.ProfitLossJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.dto.DepositHistoryTop20Dto;
import com.game.b1ingservice.postgres.jdbc.dto.SummaryDeposit;
import com.game.b1ingservice.postgres.jdbc.dto.SummaryWithdraw;
import com.game.b1ingservice.postgres.repository.DepositHistoryRepository;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.service.*;
import com.game.b1ingservice.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.game.b1ingservice.commons.Constants.DEPOSIT_STATUS.NOT_SURE;
import static com.game.b1ingservice.commons.Constants.DEPOSIT_STATUS.SUCCESS;
import static com.game.b1ingservice.commons.Constants.*;
import static com.game.b1ingservice.commons.Constants.WITHDRAW_STATUS.REJECT;
import static com.game.b1ingservice.commons.Constants.WITHDRAW_STATUS.REJECT_N_REFUND;

@Slf4j
@Service
public class DepositHistoryServiceImpl implements DepositHistoryService {

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;

    @Autowired
    private DepositHistoryJdbcRepository depositHistoryJdbcRepository;

    @Autowired
    private ProfitLossJdbcRepository profitLossJdbcRepository;

    @Autowired
    private AffiliateService affiliateService;

    @Autowired
    private WebUserRepository webUserRepository;
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private LineNotifyService lineNotifyService;

    @Autowired
    private AMBService ambService;

    @Autowired
    private BankBotService bankBotService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private WalletService walletService;

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
        List<String> statuses =  Arrays.asList(Constants.DEPOSIT_STATUS.SUCCESS, Constants.DEPOSIT_STATUS.REJECT_N_REFUND,
                Constants.DEPOSIT_STATUS.BLOCK_AUTO, Constants.DEPOSIT_STATUS.REJECT);

        if (depositHisUserReq.getStartDate() != null && depositHisUserReq.getPrevDate() != null) {
            depositHistories = depositHistoryRepository.findAllByUser_usernameAndCreatedDateBetweenAndStatusInOrderByCreatedDateDesc(username,
                    DateUtils.atStartOfDay(DateUtils.convertStartDate(depositHisUserReq.getPrevDate())).toInstant(),
                    DateUtils.atEndOfDay(DateUtils.convertEndDate(depositHisUserReq.getStartDate())).toInstant(),
                    statuses);
        } else if (depositHisUserReq.getStartDate() != null) {
            depositHistories = depositHistoryRepository.findAllByUser_usernameAndCreatedDateBetweenAndStatusInOrderByCreatedDateDesc(username,
                    DateUtils.atStartOfDay(DateUtils.convertStartDate(depositHisUserReq.getStartDate())).toInstant(),
                    DateUtils.atEndOfDay(DateUtils.convertEndDate(depositHisUserReq.getStartDate())).toInstant(),
                    statuses);
        }
        return depositHistories.stream().map(converterUser).collect(Collectors.toList());
    }

    @Override
    public List<DepositHistoryTop20Resp> findListByUsername(String username, UserPrincipal principal) {

       List<DepositHistoryTop20Dto> depositDtos = depositHistoryJdbcRepository.findTop20DepositHistory(username.toLowerCase(), principal.getAgentId());

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
    public ProfitAndLossResp findProfitAndLoss(ProfitAndLossRequest req, UserPrincipal principal) {

        ProfitAndLossResp resp = new ProfitAndLossResp();

        List<SummaryDeposit> listDeposit = profitLossJdbcRepository.sumDeposit(req, principal.getAgentId());

        if(!listDeposit.isEmpty()) {
            BigDecimal sumDeposit = new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN);
            BigDecimal sumBonus = new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN);

            for (SummaryDeposit deposit : listDeposit) {
                if (deposit.getMistakeType() == null) {
                    sumDeposit = sumDeposit.add(deposit.getDeposit());
                    sumBonus = sumBonus.add(deposit.getBonus());
                } else {
                    switch (deposit.getMistakeType()) {
                        case PROBLEM.NO_SLIP:
                            sumDeposit = sumDeposit.add(deposit.getDeposit());
                            break;
                        case PROBLEM.ADD_CREDIT:
                            sumBonus = sumBonus.add(deposit.getDeposit());
                            break;
                    }
                }
            }

            resp.setDeposit(sumDeposit);
            String sum = (sumBonus.divide(sumDeposit,2 , RoundingMode.HALF_DOWN)).multiply(new BigDecimal(100)).toString();
            resp.setBonus(sumBonus.setScale(2, RoundingMode.HALF_DOWN) + "(" + sum +  "%)");
            resp.setDepositBonus(sumDeposit.add(sumBonus));
        } else {
            resp.setDeposit(new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN));
            resp.setBonus("0.00(0.00%)");
            resp.setDepositBonus(new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN));
        }

        List<SummaryWithdraw> listWithdraw = profitLossJdbcRepository.sumWithdraw(req, principal.getAgentId());
        if(!listWithdraw.isEmpty()) {
            SummaryWithdraw withdraw = listWithdraw.get(0);
            if(null == withdraw.getWithdraw()) {
                resp.setWithdraw(new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN));
            } else {
                resp.setWithdraw(withdraw.getWithdraw());
            }
            BigDecimal sum = resp.getDeposit().subtract(resp.getWithdraw()).setScale(2, RoundingMode.HALF_DOWN);
            resp.setProfitAndLoss(sum);
        } else {
            resp.setWithdraw(new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN));
            BigDecimal sum = resp.getDeposit().subtract(resp.getWithdraw()).setScale(2, RoundingMode.HALF_DOWN);
            resp.setProfitAndLoss(sum);
        }

        return resp;

    }

    @Override
    public DepositResponse updateBlockAutoTransaction(DepositBlockStatusReq req, String usernameAdmin, Long agentId, String prefix) {
        DepositResponse response = new DepositResponse();
        response.setStatus(true);

        String lockKey = String.format("LOCK_D_BA:%s:%s", prefix, req.getDepositId());
        try {
            String status = req.getStatus();
            DepositHistory history = depositHistoryRepository.findFirstByIdAndStatusAndAgent_Id(req.getDepositId(), DEPOSIT_STATUS.BLOCK_AUTO, agentId);
            if (history == null) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00011);
            }

            // TODO Lock transaction with redis
            if (!redisService.setEX(lockKey, 1, 600)) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00015);
            }

            WebUser webUser = history.getUser();
            if (webUser == null) {
                redisService.delete(lockKey);
                throw new ErrorMessageException(Constants.ERROR.ERR_00012);
            }

            Agent agent = webUser.getAgent();

            if (SUCCESS.equals(status)) {
                // เติม credit ให้ลูกค้า
                DepositReq depositReq = DepositReq.builder().amount(history.getAmount().add(history.getBonusAmount()).setScale(2, RoundingMode.HALF_DOWN).toPlainString()).build();
                AmbResponse<DepositRes> result =  ambService.deposit(depositReq, webUser.getUsernameAmb(), agent);
                log.info("amb response : {}", result);
                if (0 == result.getCode()) {

                    walletRepository.depositCreditAndTurnOverNonMultiply(history.getAmount().add(history.getBonusAmount()), history.getTurnOver(), webUser.getId());
                    webUserRepository.updateDepositRef(result.getResult().getRef(), webUser.getId());
                    // check affiliate
                    affiliateService.earnPoint(webUser.getId(), history.getAmount(), agent.getId());

                    history.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);

                    lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_DEPOSIT,
                            webUser.getUsername(),
                            history.getAmount().setScale(2, RoundingMode.HALF_DOWN)),
                            agent.getLineToken());
                } else {
                    history.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                    history.setReason("Can't add credit at amb api");
                }

            }
            else if (REJECT.equals(status)) {
                // is reject
                // ไม่คืน เงิน
                history.setStatus(REJECT);
                response.setStatus(true);
                lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_DEPOSIT_REJECT, usernameAdmin, webUser.getUsername(),
                        history.getAmount()),
                        agent.getLineTokenWithdraw());

            }
            else if (REJECT_N_REFUND.equals(status)) {
                // is reject
                // โอนเงินคืน
                BankBotScbWithdrawCreditRequest request = new BankBotScbWithdrawCreditRequest();
                request.setAmount(history.getAmount());
                request.setAccountTo(webUser.getAccountNumber());
                request.setBankCode(webUser.getBankName());

                BankBotScbWithdrawCreditResponse bankBotResult = bankBotService.withDrawCredit(request, agent.getId());
                if (bankBotResult.getStatus()) {
                    history.setStatus(REJECT_N_REFUND);
                    response.setStatus(true);

                    lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_DEPOSIT_REJECT_RF, usernameAdmin, webUser.getUsername(),
                            history.getAmount()),
                            agent.getLineToken());
                } else {
                    response.setStatus(false);
                    response.setMessage(bankBotResult.getMessege());

                    history.setStatus(Constants.WITHDRAW_STATUS.ERROR);
                    history.setReason(bankBotResult.getMessege());
                }
            }

            history.setReason(req.getReason());
            depositHistoryRepository.save(history);

            redisService.delete(lockKey);
        } catch (Exception e) {
            log.error("updateBlockAutoTransaction", e);
            response.setStatus(false);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public DepositResponse updateNoteSureTransaction(DepositNotSureStatusReq req, String usernameAdmin, Long agentId, String prefix) {
        DepositResponse response = new DepositResponse();
        response.setStatus(true);
        String lockKey = String.format("LOCK_D_NOT_SURE:%s:%s", prefix, req.getDepositId());
        try {
            DepositHistory history = depositHistoryRepository.findFirstByIdAndStatusAndAgent_Id(req.getDepositId(), NOT_SURE, agentId);
            if (history == null) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00011);
            }

            // TODO Lock transaction with redis
            if (!redisService.setEX(lockKey, 1, 600)) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00015);
            }

            String username = req.getUsername().trim().toLowerCase(Locale.ROOT);
            String status = req.getStatus();
            history.setReason(req.getReason());

            // success => เติม credit ให้ user
            if (SUCCESS.equals(status)) {
                Optional<WebUser> webUserOpt = webUserRepository.findFirstByUsernameAndAgent_Id(username, agentId);
                if (!webUserOpt.isPresent()) {
                    redisService.delete(lockKey);
                    throw new ErrorMessageException(Constants.ERROR.ERR_00012);
                }

                WebUser webUser =  webUserOpt.get();
                DepositReq depositReq = DepositReq.builder().amount(history.getAmount().add(history.getBonusAmount()).setScale(2, RoundingMode.HALF_DOWN).toPlainString()).build();

                BigDecimal beforeAmount = walletService.updateCurrentWallet(webUser);
                AmbResponse<DepositRes> result =  ambService.deposit(depositReq, webUser.getUsernameAmb(), webUser.getAgent());
                log.info("amb response : {}", result);
                if (0 == result.getCode()) {

                    walletRepository.depositCreditAndTurnOverNonMultiply(history.getAmount().add(history.getBonusAmount()), history.getTurnOver(), webUser.getId());
                    webUserRepository.updateDepositRef(result.getResult().getRef(), webUser.getId());
                    // check affiliate
                    affiliateService.earnPoint(webUser.getId(), history.getAmount(), webUser.getAgent().getId());

                    history.setUser(webUser);
                    history.setBeforeAmount(beforeAmount);
                    history.setAfterAmount(beforeAmount.add(history.getAmount().add(history.getBonusAmount())));
                    history.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);

                    lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_ADMIN_DEPOSIT, usernameAdmin, username, history.getAmount().setScale(2, RoundingMode.HALF_DOWN)),
                            webUser.getAgent().getLineToken());


                } else {
                    history.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                    history.setReason("Can't add credit at amb api");
                }

            } else if (REJECT.equals(status)){
                // reject => reject transaction นี้
                history.setStatus(REJECT);
                response.setStatus(true);
            }

            depositHistoryRepository.save(history);
            redisService.delete(lockKey);
        }catch (Exception e) {
            log.error("updateNoteSureTransaction : {} , {}", e, req);
            response.setStatus(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public List<DepositHistoryTopAll20Resp> findLast20Transaction(Long agentId) {

        Page<DepositHistory> list = depositHistoryRepository.findByAgent_IdAndStatusOrderByCreatedDateDesc(agentId, SUCCESS,
                PageRequest.of(0, 20));

        List<DepositHistoryTopAll20Resp> result = new ArrayList<>();
        if (!list.isEmpty()) {
            for (DepositHistory depositDto : list) {
                DepositHistoryTopAll20Resp deposit = new DepositHistoryTopAll20Resp();
                if (null == depositDto.getBank()) {
                    deposit.setBankName(null);
                    deposit.setBankCode(null);
                } else {
                    deposit.setBankName(depositDto.getBank().getBankName());
                    deposit.setBankCode(depositDto.getBank().getBankCode());
                }
                deposit.setAmount(depositDto.getAmount() != null ? depositDto.getAmount().toString() : "0");
                deposit.setBonus(depositDto.getBonusAmount() != null ? depositDto.getBonusAmount().toString() : "0");
                deposit.setAddCredit(depositDto.getAmount().add(depositDto.getBonusAmount()).toString());
                deposit.setBeforeAmount(depositDto.getBeforeAmount() != null ? depositDto.getBeforeAmount().toString() : "0");
                deposit.setAfterAmount(depositDto.getAfterAmount() != null ?  depositDto.getAfterAmount().toString() : "0");

                deposit.setUsername(depositDto.getUser() != null ? depositDto.getUser().getUsername() : "");

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
                Date date = Date.from(depositDto.getCreatedDate());
                deposit.setCreatedDate(sdf.format(date));

                deposit.setReason(depositDto.getReason());

                result.add(deposit);
            }
        }

        return result;
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
        
        return new PageImpl<>(listSummary, pageable, listSummary.size());
    }

    private List<DepositListHistorySearchResponse> findUserDepositSevenDay(Page<DepositListHistorySearchResponse> searchData) {

        Map<String, SevenDay> map = new HashMap<>();

        List<DepositListHistorySearchResponse> listData = searchData.stream()
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
            List<DepositListHistorySearchResponse> list = searchData.stream()
                    .filter(obj -> obj.getUsername().equals(username)).collect(Collectors.toList());

            for(DepositListHistorySearchResponse deposit : list) {
                listSummary.add(deposit);
            }
        }

        return listSummary;
    }

    private List<DepositListHistorySearchResponse> findUserDepositSevenDay(List<DepositListHistorySearchResponse> searchData) {

        Map<String, SevenDay> map = new HashMap<>();

        List<DepositListHistorySearchResponse> listData = searchData.stream()
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
            List<DepositListHistorySearchResponse> list = searchData.stream()
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

        searchResponse.setRemark(depositHistory.getRemark());

        return searchResponse;
    };


    Function<DepositHistory, DepositHisUserRes> converterUser = depositHistory -> {
        DepositHisUserRes depHis = new DepositHisUserRes();
        depHis.setReason(depositHistory.getReason());
        depHis.setCreatedDate(depositHistory.getCreatedDate().atZone(ZoneId.systemDefault()).toLocalDateTime());
        depHis.setStatus(depositHistory.getStatus());
        depHis.setValue(depositHistory.getAmount());
        depHis.setBonus(depositHistory.getBonusAmount());
        depHis.setTotal(depositHistory.getAmount().add(depositHistory.getBonusAmount()));
        return depHis;
    };
}
