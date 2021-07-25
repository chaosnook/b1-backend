package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.amb.AmbResponse;
import com.game.b1ingservice.payload.amb.DepositReq;
import com.game.b1ingservice.payload.amb.DepositRes;
import com.game.b1ingservice.payload.bankbot.BankBotScbWithdrawCreditRequest;
import com.game.b1ingservice.payload.bankbot.BankBotScbWithdrawCreditResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.withdraw.WithDrawResponse;
import com.game.b1ingservice.payload.withdraw.WithdrawHisUserReq;
import com.game.b1ingservice.payload.withdraw.WithdrawHisUserRes;
import com.game.b1ingservice.payload.withdrawhistory.*;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.entity.WithdrawHistory;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.postgres.repository.WithdrawHistoryRepository;
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

import static com.game.b1ingservice.commons.Constants.DEPOSIT_STATUS.SUCCESS;
import static com.game.b1ingservice.commons.Constants.*;
import static com.game.b1ingservice.commons.Constants.WITHDRAW_STATUS.*;

@Slf4j
@Service
public class WithdrawHistoryServiceImpl implements WithdrawHistoryService {

    @Autowired
    private WithdrawHistoryRepository withdrawHistoryRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    private BankBotService bankBotService;

    @Autowired
    private LineNotifyService lineNotifyService;

    @Autowired
    private AMBService ambService;

    @Autowired
    private RedisService redisService;

    @Override
    public WithdrawHistory saveHistory(WithdrawHistory withdrawHistory) {
        return withdrawHistoryRepository.save(withdrawHistory);
    }

    @Override
    public List<WithdrawHisUserRes> searchByUser(WithdrawHisUserReq withDrawRequest, String username) {
        List<WithdrawHistory> depositHistories = new ArrayList<>();
        List<String> statuses = Arrays.asList(SUCCESS, REJECT_N_REFUND, BLOCK_AUTO, REJECT);

        if (withDrawRequest.getStartDate() != null && withDrawRequest.getPrevDate() != null) {
            depositHistories = withdrawHistoryRepository.findAllByUser_usernameAndCreatedDateBetweenAndStatusInOrderByCreatedDateDesc(username,
                    DateUtils.atStartOfDay(DateUtils.convertStartDate(withDrawRequest.getPrevDate())).toInstant(),
                    DateUtils.atEndOfDay(DateUtils.convertEndDate(withDrawRequest.getStartDate())).toInstant(),
                    statuses);
        } else if (withDrawRequest.getStartDate() != null) {
            depositHistories = withdrawHistoryRepository.findAllByUser_usernameAndCreatedDateBetweenAndStatusInOrderByCreatedDateDesc(username,
                    DateUtils.atStartOfDay(DateUtils.convertStartDate(withDrawRequest.getStartDate())).toInstant(),
                    DateUtils.atEndOfDay(DateUtils.convertEndDate(withDrawRequest.getStartDate())).toInstant(),
                    statuses);
        }
        return depositHistories.stream().map(converterUser).collect(Collectors.toList());
    }

    @Override
    public List<WithdrawHistoryByUserIdResp> findListByUserId(Long userId, UserPrincipal principal) {
        List<WithdrawHistory> list = withdrawHistoryRepository.findByUser_IdAndUser_Agent_PrefixOrderByCreatedDateDesc(userId, principal.getPrefix());
        List<WithdrawHistoryByUserIdResp> result = new ArrayList<>();
        if (!list.isEmpty()) {
            for (WithdrawHistory withdrawDto : list) {
                WithdrawHistoryByUserIdResp withdraw = new WithdrawHistoryByUserIdResp();
                if (null == withdrawDto.getBank()) {
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
                withdraw.setQrCode(withdrawDto.getQrCode());

                result.add(withdraw);
            }
        }

        return result;
    }

    Function<WithdrawHistory, WithdrawHisUserRes> converterUser = withdrawHistory -> {
        WithdrawHisUserRes witHis = new WithdrawHisUserRes();
        witHis.setReason(withdrawHistory.getReason());
        witHis.setQrCode(withdrawHistory.getQrCode());
        witHis.setCreatedDate(withdrawHistory.getCreatedDate().atZone(ZoneId.systemDefault()).toLocalDateTime());
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
        for (WithdrawSummaryHistorySearchResponse withdraw : searchData) {

            WithdrawSummaryHistorySearchResponse summary = new WithdrawSummaryHistorySearchResponse();

            if (!map.containsKey(withdraw.getBankName())) {

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
        if (null == withdrawHistory.getBank()) {
            searchResponse.setBankName(null);
            searchResponse.setBankCode(null);
        } else {
            searchResponse.setBankName(withdrawHistory.getBank().getBankName());
            searchResponse.setBankCode(withdrawHistory.getBank().getBankCode());
        }
        if (null == withdrawHistory.getUser()) {
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
        searchResponse.setQrCode(withdrawHistory.getQrCode());
        if (null == withdrawHistory.getAdmin()) {
            searchResponse.setAdmin(null);
        } else {
            searchResponse.setAdmin(withdrawHistory.getAdmin().getUsername());
        }
        searchResponse.setRemainBalance(withdrawHistory.getRemainBalance());

        searchResponse.setBeforeTransfer(withdrawHistory.getBeforeTransfer());
        searchResponse.setAfterTransfer(withdrawHistory.getAfterTransfer());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");

        searchResponse.setCreatedDate(sdf.format(Date.from(withdrawHistory.getCreatedDate())));
        searchResponse.setUpdatedDate(sdf.format(Date.from(withdrawHistory.getUpdatedDate())));
        searchResponse.setCreatedBy(withdrawHistory.getAudit().getCreatedBy());
        searchResponse.setUpdatedBy(withdrawHistory.getAudit().getUpdatedBy());
        searchResponse.setDeleteFlag(withdrawHistory.getDeleteFlag());
        searchResponse.setVersion(withdrawHistory.getVersion());

        return searchResponse;
    };

    Function<WithdrawHistory, WithdrawSummaryHistorySearchResponse> converter2 = withdrawHistory -> {
        WithdrawSummaryHistorySearchResponse searchResponse = new WithdrawSummaryHistorySearchResponse();
        if (null == withdrawHistory.getBank()) {
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

    @Override
    public void updateStatus(WithdrawHistoryUpdateStatusReq req, Long agentId) {
        withdrawHistoryRepository.updateStatus(req.getStatus(), req.getId(), req.getAmount(), agentId);
    }

    @Override
    public WithDrawResponse updateBlockAutoTransaction(WithdrawBlockStatusReq req, String usernameAdmin, Long agentId, String prefix) {
        WithDrawResponse response = new WithDrawResponse();
        String lockKey = String.format("LOCK_W_BA:%s:%s", prefix, req.getWithdrawId());
        try {
            String status = req.getStatus();

            WithdrawHistory history = withdrawHistoryRepository.findFirstByIdAndStatusAndAgent_Id(req.getWithdrawId(), BLOCK_AUTO, agentId);
            if (history == null) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00011);
            }

            if (!redisService.setEX(lockKey, 1, 3600)) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00015);
            }

            WebUser webUser = history.getUser();
            if (webUser == null) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00012);
            }

            Agent agent = webUser.getAgent();

            if (SUCCESS.equals(status)) {// โอนเงินให้ถูกค้า
                BankBotScbWithdrawCreditRequest request = new BankBotScbWithdrawCreditRequest();
                request.setAmount(history.getAmount());
                request.setAccountTo(webUser.getAccountNumber());
                request.setBankCode(webUser.getBankName());

                BankBotScbWithdrawCreditResponse bankBotResult = bankBotService.withDrawCredit(request, agent.getId());
                log.info("bankbot withdraw response {}", bankBotResult);
                history.setIsAuto(false);

                if (bankBotResult.getStatus()) {
                    response.setStatus(true);
                    history.setStatus(SUCCESS);
                    history.setRemainBalance(bankBotResult.getRemainingBalance());
                    history.setQrCode(bankBotResult.getQrString());

                    lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_WITHDRAW_APPROVE + MESSAGE_WITHDRAW_REMAIN,
                            usernameAdmin, webUser.getUsername(), history.getAmount(), bankBotResult.getRemainingBalance()),
                            agent.getLineTokenWithdraw());
                } else {
                    response.setStatus(false);
                    response.setMessage(bankBotResult.getMessege());

                    history.setStatus(WITHDRAW_STATUS.ERROR);
                    history.setReason(bankBotResult.getMessege());
                }
            } else if (REJECT.equals(status)) {
                // is reject
                // ไม่คืน credit
                history.setStatus(REJECT);
                response.setStatus(true);

                lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_WITHDRAW_REJECT, usernameAdmin, webUser.getUsername(),
                        history.getAmount()),
                        agent.getLineTokenWithdraw());

            } else if (REJECT_N_REFUND.equals(status)) {
                // is reject
                // คืน credit
                BigDecimal credit = history.getAmount().setScale(2, RoundingMode.HALF_DOWN);
                AmbResponse<DepositRes> ambResponse = ambService.deposit(DepositReq.builder()
                        .amount(credit.toPlainString())
                        .build(), webUser.getUsernameAmb(), agent);

                if (ambResponse.getCode() == 0) {
                    history.setStatus(REJECT_N_REFUND);
                    walletRepository.depositCredit(credit, webUser.getId());
                    webUserRepository.updateDepositRef(ambResponse.getResult().getRef(), webUser.getId());

                    response.setStatus(true);

                    lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_WITHDRAW_REJECT_RF, usernameAdmin, webUser.getUsername(),
                            history.getAmount()),
                            agent.getLineTokenWithdraw());
                } else {
                    response.setStatus(false);
                    response.setMessage("Can't add credit at amb api");

                    history.setStatus(WITHDRAW_STATUS.ERROR);
                    history.setReason("Can't add credit at amb api");
                }
            } else if (SELF_TRANSFER.equals(status)) {
                history.setStatus(SUCCESS);
                history.setBeforeTransfer(req.getBeforeTransfer());
                history.setAfterTransfer(req.getAfterTransfer());
                response.setStatus(true);

                lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_WITHDRAW_SELF, usernameAdmin, webUser.getUsername(),
                        history.getAmount()),
                        agent.getLineTokenWithdraw());
            }

            history.setReason(req.getReason());
            this.saveHistory(history);

            redisService.delete(lockKey);

        } catch (Exception e) {
            log.error("updateBlockAutoTransaction", e);
            response.setStatus(false);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public List<WithdrawHistoryTopAll20Resp> findLast20Transaction(Long agentId) {
        Page<WithdrawHistory> list = withdrawHistoryRepository.findByUser_Agent_IdAndStatusOrderByCreatedDateDesc(
                agentId, SUCCESS, PageRequest.of(0, 20));

        List<WithdrawHistoryTopAll20Resp> response = new ArrayList<>();
        if (!list.isEmpty()) {
            for (WithdrawHistory withdrawDto : list) {
                WithdrawHistoryTopAll20Resp withdraw = new WithdrawHistoryTopAll20Resp();
                withdraw.setUsername(withdrawDto.getUser().getUsername());
                if (null == withdrawDto.getBank()) {
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
                withdraw.setQrCode(withdrawDto.getQrCode());

                response.add(withdraw);
            }
        }
        return response;
    }


    //TODO ให้ bank bot โอนเงินอีกรอบ
    @Override
    public WithDrawResponse refreshTransaction(RefreshTransactionReq req, String usernameAdmin, Long agentId, String prefix) {
        WithDrawResponse response = new WithDrawResponse();
        String lockKey = String.format("LOCK_REFRESH:%s:%s", prefix, req.getWithdrawId());
        try {
            WithdrawHistory history = withdrawHistoryRepository.findFirstByIdAndStatusAndAgent_Id(req.getWithdrawId(), ERROR, agentId);
            if (history == null) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00011);
            }

            if (!redisService.setEX(lockKey, 1, 3600)) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00015);
            }

            WebUser webUser = history.getUser();
            if (webUser == null) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00012);
            }

            Agent agent = webUser.getAgent();

            BankBotScbWithdrawCreditRequest request = new BankBotScbWithdrawCreditRequest();
            request.setAmount(history.getAmount());
            request.setAccountTo(webUser.getAccountNumber());
            request.setBankCode(webUser.getBankName());

            BankBotScbWithdrawCreditResponse bankBotResult = bankBotService.withDrawCredit(request, agent.getId());
            log.info("bankbot withdraw response {}", bankBotResult);
            history.setIsAuto(false);

            if (bankBotResult.getStatus()) {
                response.setStatus(true);
                history.setStatus(SUCCESS);
                history.setRemainBalance(bankBotResult.getRemainingBalance());
                history.setQrCode(bankBotResult.getQrString());
                history.setReason("");

                lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_RE_WITHDRAW + MESSAGE_WITHDRAW_REMAIN,
                        usernameAdmin, webUser.getUsername(), history.getAmount(), bankBotResult.getRemainingBalance()),
                        agent.getLineTokenWithdraw());
            } else {
                response.setStatus(false);
                response.setMessage(bankBotResult.getMessege());

                history.setStatus(WITHDRAW_STATUS.ERROR);
                history.setReason(bankBotResult.getMessege());

                lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_RE_WITHDRAW_ERROR,
                        usernameAdmin, webUser.getUsername(), history.getAmount(), bankBotResult.getMessege()),
                        agent.getLineTokenWithdraw());
            }


            this.saveHistory(history);

            redisService.delete(lockKey);
        } catch (Exception e) {
            log.error("refreshTransaction", e);
            response.setStatus(false);
            response.setMessage(e.getMessage());

            redisService.delete(lockKey);
        }
        return response;
    }

    @Override
    public WithDrawResponse selfTransfer(SelfTransactionReq req, String usernameAdmin, Long agentId, String prefix) {
        WithDrawResponse response = new WithDrawResponse();
        String lockKey = String.format("LOCK_W_BA:%s:%s", prefix, req.getWithdrawId());
        try {
            WithdrawHistory history = withdrawHistoryRepository.findFirstByIdAndStatusAndAgent_Id(req.getWithdrawId(), WITHDRAW_STATUS.ERROR, agentId);
            if (history == null) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00011);
            }

            if (!redisService.setEX(lockKey, 1, 3600)) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00015);
            }

            WebUser webUser = history.getUser();
            if (webUser == null) {
                throw new ErrorMessageException(Constants.ERROR.ERR_00012);
            }

            Agent agent = webUser.getAgent();
            history.setStatus(SUCCESS);
            history.setBeforeTransfer(req.getBeforeTransfer());
            history.setAfterTransfer(req.getAfterTransfer());
            response.setStatus(true);
            history.setReason("");

            lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_WITHDRAW_SELF, usernameAdmin, webUser.getUsername(),
                    history.getAmount()),
                    agent.getLineTokenWithdraw());

            this.saveHistory(history);

            redisService.delete(lockKey);
        } catch (Exception e) {
            log.error("selfTransfer", e);
            response.setStatus(false);
            response.setMessage(e.getMessage());
        }

        return response;
    }


}
