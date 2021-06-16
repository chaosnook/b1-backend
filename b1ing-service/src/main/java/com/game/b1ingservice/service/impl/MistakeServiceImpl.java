package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.amb.*;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.misktake.MistakeReq;
import com.game.b1ingservice.payload.misktake.MistakeSearchListRes;
import com.game.b1ingservice.payload.misktake.MistakeSearchReq;
import com.game.b1ingservice.payload.misktake.MistakeSearchSummaryRes;
import com.game.b1ingservice.postgres.entity.*;
import com.game.b1ingservice.postgres.repository.*;
import com.game.b1ingservice.service.AMBService;
import com.game.b1ingservice.service.AffiliateService;
import com.game.b1ingservice.service.LineNotifyService;
import com.game.b1ingservice.service.MistakeService;
import com.game.b1ingservice.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.game.b1ingservice.commons.Constants.AGENT_CONFIG.COUNT_WITHDRAW;
import static com.game.b1ingservice.commons.Constants.AGENT_CONFIG.LIMIT_WITHDRAW;
import static com.game.b1ingservice.commons.Constants.DATE_FORMAT;
import static com.game.b1ingservice.commons.Constants.MESSAGE_ADMIN_DEPOSIT;

@Service
@Slf4j
public class MistakeServiceImpl implements MistakeService {

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    private AMBService ambService;

    @Autowired
    private DepositHistoryRepository depositHistoryRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WithdrawHistoryRepository withdrawHistoryRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private AffiliateService affiliateService;

    @Autowired
    private LineNotifyService lineNotifyService;

    @Override
    public void createMistake(MistakeReq mistakeReq, UserPrincipal principal) {
        Optional<WebUser> opt = webUserRepository.findFirstByUsernameAndAgent_Id(mistakeReq.getUsername(), principal.getAgentId());

        if (!opt.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01127);
        }

        Optional<AdminUser> adminOpt = adminUserRepository.findById(principal.getId());
        if (!adminOpt.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01127);
        }

        // Mistake limit
        Integer isLimit = adminOpt.get().getLimit();
        if (isLimit != null && isLimit == 1 && adminOpt.get().getMistakeLimit() >= adminOpt.get().getLimit()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01134);
        }

        WebUser user = opt.get();
        BigDecimal credit = mistakeReq.getCredit().setScale(2, RoundingMode.HALF_DOWN);
        String username = user.getUsername();
        Agent agent = user.getAgent();

        // update mistake
        adminUserRepository.addMistake(adminOpt.get().getId());

        Wallet wallet = user.getWallet();
        BigDecimal beforeAmount = wallet.getCredit();
        BigDecimal afterAmount = beforeAmount.add(credit);

        AmbResponse<DepositRes> ambResponse;

        switch (mistakeReq.getType()) {
            case Constants.PROBLEM.NO_SLIP:
                ambResponse = ambService.deposit(DepositReq.builder()
                        .amount(credit.toPlainString()).build(), user.getUsernameAmb(), agent);
                if (ambResponse.getCode() == 0) {

                    lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_ADMIN_DEPOSIT, principal.getUsername(),
                            username, credit.toPlainString()) ,
                            agent.getLineToken());

                    walletRepository.depositCredit(credit, user.getId());
                    webUserRepository.updateDepositRef(ambResponse.getResult().getRef(), user.getId());
                    // check affiliate
                    affiliateService.earnPoint(wallet.getUser().getId(), credit, agent.getId());

                    DepositHistory depositHistory = new DepositHistory();
                    depositHistory.setAmount(credit);
                    depositHistory.setBeforeAmount(beforeAmount);
                    depositHistory.setAfterAmount(afterAmount);
                    depositHistory.setUser(user);
                    depositHistory.setMistakeType(Constants.PROBLEM.NO_SLIP);
                    depositHistory.setReason(mistakeReq.getReason());
                    depositHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);
                    depositHistory.setBonusAmount(BigDecimal.ZERO);
                    depositHistory.setAdmin(adminOpt.get());
                    depositHistory.setAgent(agent);
                    depositHistoryRepository.save(depositHistory);
                } else {
                    throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
                }

                break;

            case Constants.PROBLEM.ADD_CREDIT:
                ambResponse = ambService.deposit(DepositReq.builder()
                        .amount(credit.toPlainString()).build(), user.getUsernameAmb(), agent);

                if (ambResponse.getCode() == 0) {

                    lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_ADMIN_DEPOSIT,principal.getUsername(), username ,credit.toPlainString()) ,
                            agent.getLineToken());

                    walletRepository.depositCreditAndTurnOver(credit, credit, mistakeReq.getTurnOver(), user.getId());
                    webUserRepository.updateDepositRef(ambResponse.getResult().getRef(), user.getId());
                    // check affiliate
                    affiliateService.earnPoint(wallet.getUser().getId(), credit, agent.getId());

                    DepositHistory depositHistory = new DepositHistory();
                    depositHistory.setAmount(credit);
                    depositHistory.setBeforeAmount(beforeAmount);
                    depositHistory.setAfterAmount(afterAmount);
                    depositHistory.setUser(user);
                    depositHistory.setMistakeType(Constants.PROBLEM.ADD_CREDIT);
                    depositHistory.setReason(mistakeReq.getReason());
                    depositHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);
                    depositHistory.setBonusAmount(BigDecimal.ZERO);
                    depositHistory.setAdmin(adminOpt.get());
                    depositHistory.setAgent(agent);
                    depositHistoryRepository.save(depositHistory);
                } else {
                    throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
                }

                break;

            case Constants.PROBLEM.CUT_CREDIT:
                AmbResponse<WithdrawRes> withdrawRes = ambService.withdraw(WithdrawReq.builder()
                        .amount(credit.toPlainString()).build(), user.getUsernameAmb(), agent);

                if (withdrawRes.getCode() == 0) {
                    walletRepository.withDrawCredit(credit, user.getId());

                    WithdrawHistory withdrawHistory = new WithdrawHistory();
                    withdrawHistory.setAmount(credit);
                    withdrawHistory.setBeforeAmount(beforeAmount);
                    withdrawHistory.setAfterAmount(afterAmount);
                    withdrawHistory.setUser(user);
                    withdrawHistory.setMistakeType(Constants.PROBLEM.CUT_CREDIT);
                    withdrawHistory.setReason(mistakeReq.getReason());
                    withdrawHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);
                    withdrawHistory.setAdmin(adminOpt.get());
                    withdrawHistory.setAgent(agent);
                    withdrawHistoryRepository.save(withdrawHistory);
                } else {
                    throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
                }
                break;
            default:
                throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

    }

    @Override
    public List<MistakeSearchListRes> findByCriteria(MistakeSearchReq req, UserPrincipal principal) {
        Instant instantStart = DateUtils.convertStartDateTimeSec(req.getStartDate()).toInstant();
        Instant instantEnd = DateUtils.convertEndDateTimeSec(req.getEndDate()).toInstant();
        String type = req.getType();

        List<String> types = new ArrayList<>();
        if (type.equals("ALL")) {
            types.addAll(Arrays.asList(Constants.PROBLEM.CUT_CREDIT, Constants.PROBLEM.ADD_CREDIT, Constants.PROBLEM.NO_SLIP));
        } else {
            types.add(type.toUpperCase(Locale.ROOT));
        }

        Optional<Agent> agent = agentRepository.findById(principal.getAgentId());
        if (!agent.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }


        List<DepositHistory> list = new ArrayList<>();
        List<WithdrawHistory> listWithdraw = new ArrayList<>();

        if (types.contains(Constants.PROBLEM.ADD_CREDIT) || types.contains(Constants.PROBLEM.NO_SLIP)) {
            list = depositHistoryRepository.findAllByAgentAndCreatedDateBetweenAndMistakeTypeInOrderByCreatedDateDesc(agent.get(), instantStart, instantEnd, types);
        }

        if (types.contains(Constants.PROBLEM.CUT_CREDIT)) {
            listWithdraw = withdrawHistoryRepository.findAllByAgentAndCreatedDateBetweenAndMistakeTypeInOrderByCreatedDateDesc(agent.get(), instantStart, instantEnd, Collections.singletonList(Constants.PROBLEM.CUT_CREDIT));
        }

        List<MistakeSearchListRes> resList = list.stream().map(converter).collect(Collectors.toList());
        resList.addAll(listWithdraw.stream().map(converterWith).collect(Collectors.toList()));
        return resList;
    }

    @Override
    public MistakeSearchSummaryRes summaryData(List<MistakeSearchListRes> resList) {

        MistakeSearchSummaryRes summaryRes = new MistakeSearchSummaryRes();
        BigDecimal noSlip = BigDecimal.valueOf(0);
        BigDecimal cutCredit = BigDecimal.valueOf(0);
        BigDecimal addCredit = BigDecimal.valueOf(0);

        for (MistakeSearchListRes res : resList) {
            switch (res.getMistakeType()) {
                case Constants.PROBLEM.NO_SLIP:
                    noSlip = noSlip.add(res.getAmount());
                    break;
                case Constants.PROBLEM.CUT_CREDIT:
                    cutCredit = cutCredit.add(res.getAmount());
                    break;
                case Constants.PROBLEM.ADD_CREDIT:
                    addCredit = addCredit.add(res.getAmount());
                    break;
            }
        }

        summaryRes.setNoSlip(noSlip);
        summaryRes.setCutCredit(cutCredit);
        summaryRes.setAddCredit(addCredit);

        return summaryRes;
    }

    @Override
    public void clearLimit() {
        adminUserRepository.clearMistakeLimit();
    }


    private boolean checkCanLimit(List<Config> configs, Integer countMistake) {
        boolean isCheck = false;
        Integer max = 0;
        for (Config config : configs) {
            if (config.getParameter().equals(LIMIT_WITHDRAW)) {
                isCheck = Boolean.parseBoolean(config.getValue());
            } else if (config.getParameter().equals(COUNT_WITHDRAW)) {
                max = Integer.valueOf(config.getValue());
            }
        }
        if (isCheck) {
            return countMistake < max;
        }

        return true;
    }


    Function<DepositHistory, MistakeSearchListRes> converter = history -> {
        MistakeSearchListRes res = new MistakeSearchListRes();
        res.setBankName(history.getMistakeType());
        res.setUsername(history.getUser().getUsername());
        res.setMistakeType(history.getMistakeType());
        res.setAmount(history.getAmount());
        res.setAfterAmount(history.getAfterAmount());
        res.setBeforeAmount(history.getBeforeAmount());
        res.setReason(history.getReason());
        res.setCreatedDate(DATE_FORMAT.format(history.getCreatedDate().toEpochMilli()));
        res.setCreatedBy(history.getAdmin().getUsername());

        return res;
    };


    Function<WithdrawHistory, MistakeSearchListRes> converterWith = history -> {
        MistakeSearchListRes res = new MistakeSearchListRes();
        res.setBankName(history.getMistakeType());
        res.setMistakeType(history.getMistakeType());
        res.setUsername(history.getUser().getUsername());
        res.setAmount(history.getAmount());
        res.setAfterAmount(history.getAfterAmount());
        res.setBeforeAmount(history.getBeforeAmount());
        res.setReason(history.getReason());
        res.setCreatedDate(DATE_FORMAT.format(history.getCreatedDate().toEpochMilli()));
        res.setCreatedBy(history.getAdmin().getUsername());

        return res;
    };
}
