package com.game.b1ingservice.service.impl;

import com.drew.metadata.Age;
import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.amb.AmbResponse;
import com.game.b1ingservice.payload.amb.CreateUserRes;
import com.game.b1ingservice.payload.amb.WithdrawReq;
import com.game.b1ingservice.payload.amb.WithdrawRes;
import com.game.b1ingservice.payload.withdraw.WithDrawRequest;
import com.game.b1ingservice.payload.withdraw.WithDrawResponse;
import com.game.b1ingservice.postgres.entity.*;
import com.game.b1ingservice.postgres.repository.ConfigRepository;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.postgres.repository.WithdrawHistoryRepository;
import com.game.b1ingservice.service.AMBService;
import com.game.b1ingservice.service.WithDrawService;
import com.game.b1ingservice.service.WithdrawHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static com.game.b1ingservice.commons.Constants.AGENT_CONFIG.MIN_WITHDRAW_CREDIT;


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


    @Override
    public WithDrawResponse withdraw(WithDrawRequest withDrawRequest, String username, String prefix) {
        Wallet wallet = walletRepository.findFirstByUser_UsernameAndUser_Agent_Prefix(username, prefix);
        if (wallet == null) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00011);
        }

        WebUser webUser = wallet.getUser();
        Bank bank = wallet.getBank();

        BigDecimal creditWithDraw = withDrawRequest.getCreditWithDraw();

        WithdrawHistory withdrawHistory = new WithdrawHistory();
        withdrawHistory.setAmount(creditWithDraw);
        withdrawHistory.setBeforeAmount(wallet.getCredit());
        withdrawHistory.setUser(webUser);
        withdrawHistory.setBank(bank);
        withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.PENDING);
        withdrawHistory = withdrawHistoryService.saveHistory(withdrawHistory);

        if (wallet.getCredit().compareTo(creditWithDraw) < 0) {
            withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.ERROR);
            withdrawHistory.setReason(Constants.ERROR.ERR_04005.msg);
            withdrawHistoryService.saveHistory(withdrawHistory);
            throw new ErrorMessageException(Constants.ERROR.ERR_04005);
        }

        Optional<Config> minWithdrawConf = configRepository.findFirstByParameterAndAgent(MIN_WITHDRAW_CREDIT, webUser.getAgent());
        boolean isAuto = true;
        if (minWithdrawConf.isPresent()) {
            Config config = minWithdrawConf.get();
            BigDecimal minW = new BigDecimal(config.getValue());
            isAuto = creditWithDraw.compareTo(minW) >= 0;
        }

        AmbResponse<WithdrawRes> ambRes = ambService.withdraw(
                WithdrawReq.builder().amount(creditWithDraw.setScale(2, RoundingMode.HALF_DOWN).toPlainString()).build(),
                username, webUser.getAgent());

        if (ambRes.getCode() != 0) {
            withdrawHistory.setReason("API AMB Error");
            withdrawHistoryService.saveHistory(withdrawHistory);
            throw new ErrorMessageException(Constants.ERROR.ERR_04005);
        }

        WithDrawResponse response = new WithDrawResponse();
        int withdrawResult = this.withDrawCredit(creditWithDraw, webUser.getId());
        response.setStatus(withdrawResult > 0);
        if (withdrawResult > 0) {
            withdrawHistory.setAfterAmount(wallet.getCredit().subtract(creditWithDraw));
            withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.SUCCESS);
            if (isAuto) {
                //TODO call bot ถอนเงิน
            } else {
                withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.PENDING_APPROVE);
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
