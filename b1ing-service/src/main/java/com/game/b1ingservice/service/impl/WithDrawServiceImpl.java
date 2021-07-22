package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.amb.AmbResponse;
import com.game.b1ingservice.payload.amb.WithdrawReq;
import com.game.b1ingservice.payload.amb.WithdrawRes;
import com.game.b1ingservice.payload.bankbot.BankBotScbWithdrawCreditRequest;
import com.game.b1ingservice.payload.bankbot.BankBotScbWithdrawCreditResponse;
import com.game.b1ingservice.payload.withdraw.WithDrawRequest;
import com.game.b1ingservice.payload.withdraw.WithDrawResponse;
import com.game.b1ingservice.postgres.entity.*;
import com.game.b1ingservice.postgres.repository.ConfigRepository;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static com.game.b1ingservice.commons.Constants.AGENT_CONFIG.MAX_AUTO_WITHDRAW;
import static com.game.b1ingservice.commons.Constants.AGENT_CONFIG.MIN_WITHDRAW_CREDIT;
import static com.game.b1ingservice.commons.Constants.ERROR.ERR_00013;
import static com.game.b1ingservice.commons.Constants.*;
import static com.game.b1ingservice.commons.Constants.MESSAGE.MSG_00001;


@Slf4j
@Service
public class WithDrawServiceImpl implements WithDrawService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WithdrawHistoryService withdrawHistoryService;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private AMBService ambService;

    @Autowired
    private BankBotService bankBotService;

    @Autowired
    private LineNotifyService lineNotifyService;

    @Autowired
    private AgentService agentService;

    @Override
    public WithDrawResponse withdraw(WithDrawRequest withDrawRequest, String username, Long agentId) {
        Wallet wallet = walletRepository.findFirstByUser_UsernameAndUser_Agent_Id(username, agentId);
        if (wallet == null) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00011);
        }

        WebUser webUser = wallet.getUser();
        Agent agent = webUser.getAgent();

        agentService.checkCanWithdraw(agent, webUser);

        BigDecimal creditWithDraw = withDrawRequest.getCreditWithDraw();

        WithdrawHistory withdrawHistory = new WithdrawHistory();
        withdrawHistory.setAmount(creditWithDraw);
        withdrawHistory.setBeforeAmount(wallet.getCredit());
        withdrawHistory.setUser(webUser);
        withdrawHistory.setIsAuto(false);
        withdrawHistory.setBank(wallet.getBank());
        withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.PENDING);
        withdrawHistory.setAgent(agent);
        withdrawHistory = withdrawHistoryService.saveHistory(withdrawHistory);

        if (wallet.getCredit().compareTo(creditWithDraw) < 0) {
            withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.ERROR);
            withdrawHistory.setReason(Constants.ERROR.ERR_04005.msg);
            withdrawHistoryService.saveHistory(withdrawHistory);
            throw new ErrorMessageException(Constants.ERROR.ERR_04005);
        }


        Optional<Config> minWithdrawConf = configRepository.findFirstByParameterAndAgent(MIN_WITHDRAW_CREDIT, agent);
        boolean isAuto = true;
        if (minWithdrawConf.isPresent()) {
            Config config = minWithdrawConf.get();
            BigDecimal minW = new BigDecimal(config.getValue());
            isAuto = creditWithDraw.compareTo(minW) >= 0;
        }

        // check max
        Optional<Config> maxWithdrawConf = configRepository.findFirstByParameterAndAgent(MAX_AUTO_WITHDRAW, agent);
        if (maxWithdrawConf.isPresent()) {
            Config configMx = maxWithdrawConf.get();
            BigDecimal maxW = new BigDecimal(configMx.getValue());
            isAuto = creditWithDraw.compareTo(maxW) <= 0;
        }



        // อนุญาติถอน auto ?
        AmbResponse<WithdrawRes> ambRes = ambService.withdraw(
                WithdrawReq.builder().amount(creditWithDraw.setScale(2, RoundingMode.HALF_DOWN).toPlainString()).build(),
                webUser.getUsernameAmb(), webUser.getAgent());

        if (ambRes.getCode() != 0) {
            withdrawHistory.setReason("API AMB Error");
            withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.ERROR);
            withdrawHistoryService.saveHistory(withdrawHistory);
            throw new ErrorMessageException(Constants.ERROR.ERR_04005);
        }


        WithDrawResponse response = new WithDrawResponse();
        response.setMessage(MSG_00001.msg);

        int withdrawResult = this.withDrawCredit(creditWithDraw, webUser.getId());
        response.setStatus(withdrawResult > 0);
        if (withdrawResult > 0) {
            withdrawHistory.setAfterAmount(wallet.getCredit().subtract(creditWithDraw));

            if (webUser.getWithdrawAuto() && isAuto) {
                // bank ที่จะโอนเงินให้ลูกค้า
                BankBotScbWithdrawCreditRequest request = new BankBotScbWithdrawCreditRequest();
                request.setAmount(creditWithDraw);
                request.setAccountTo(webUser.getAccountNumber());
                request.setBankCode(webUser.getBankName());
                BankBotScbWithdrawCreditResponse bankBotResult = bankBotService.withDrawCredit(request, agent.getId());
                log.info("bankbot withdraw response {}", bankBotResult);
                withdrawHistory.setIsAuto(true);
                if (bankBotResult.getStatus()) {
                    withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.SUCCESS);
                    withdrawHistory.setQrCode(bankBotResult.getQrString());
                    withdrawHistory.setRemainBalance(bankBotResult.getRemainingBalance());

                    lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_WITHDRAW + MESSAGE_WITHDRAW_REMAIN, webUser.getUsername(), creditWithDraw, bankBotResult.getRemainingBalance()),
                            wallet.getUser().getAgent().getLineTokenWithdraw());
                } else {
                    withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.ERROR);
                    withdrawHistory.setReason(bankBotResult.getMessege());

                    lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_WITHDRAW_ERROR, webUser.getUsername(), creditWithDraw),
                            wallet.getUser().getAgent().getLineTokenWithdraw());
                }
            } else {
                //  block auto
                withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.BLOCK_AUTO);
                response.setMessage(ERR_00013.msg);
                lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_WITHDRAW_BLOCK, webUser.getUsername(), creditWithDraw),
                        wallet.getUser().getAgent().getLineTokenWithdraw());
            }
        } else {
            withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.ERROR);
        }

        withdrawHistoryService.saveHistory(withdrawHistory);
        return response;
    }

    private int withDrawCredit(BigDecimal credit, Long userId) {
        try {
            return walletRepository.withDrawCredit(credit, userId);
        } catch (Exception e) {
            log.error("withDrawCredit", e);
        }
        return 0;
    }
}
