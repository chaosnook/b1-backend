package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.withdraw.WithDrawRequest;
import com.game.b1ingservice.payload.withdraw.WithDrawResponse;
import com.game.b1ingservice.postgres.entity.*;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.postgres.repository.WithdrawHistoryRepository;
import com.game.b1ingservice.service.WithDrawService;
import com.game.b1ingservice.service.WithdrawHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Slf4j
@Service
public class WithDrawServiceImpl implements WithDrawService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WithdrawHistoryService withdrawHistoryService;


    @Override
    public WithDrawResponse withdraw(WithDrawRequest withDrawRequest, String username, String prefix) {
        Wallet wallet = walletRepository.findFirstByUser_UsernameAndUser_Agent_Prefix(username, prefix);
        if (wallet == null) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00011);
        }

        WebUser webUser = wallet.getUser();
        Bank bank = wallet.getBank();

        BigDecimal creditWithDraw = withDrawRequest.getCreditWithDraw();

        //TODO create withdraw history pending
        WithdrawHistory withdrawHistory = new WithdrawHistory();
        withdrawHistory.setAmount(creditWithDraw);
        withdrawHistory.setBeforeAmount(wallet.getCredit());
        withdrawHistory.setUser(webUser);
        withdrawHistory.setBank(bank);
        withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.PENDING);
        withdrawHistory = withdrawHistoryService.saveHistory(withdrawHistory);

        if (wallet.getCredit().compareTo(creditWithDraw) < 0) {
            //TODO update withdraw history error
            withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.ERROR);
            withdrawHistory.setReason(Constants.ERROR.ERR_04005.msg);
            withdrawHistoryService.saveHistory(withdrawHistory);
            throw new ErrorMessageException(Constants.ERROR.ERR_04005);
        }

        //TODO condition if (credit withdraw มากกว่าจำนวนเงินสูงสุดที่จะออโต้ได้)
        // true => หักเงินแล้วจาก ระบบ และ AMB แต่ยังไม่โอนเงินจริงรอ admin approve
        // false => ตัดเงินแล้ว โอนเงินปกติ
        //TODO call withdraw credit to AMB
        int withdrawResult = this.withDrawCredit(creditWithDraw, webUser.getId());
        WithDrawResponse response = new WithDrawResponse();
        response.setStatus(withdrawResult > 0);

        if (withdrawResult > 0) {
            //TODO update withdraw history success
            withdrawHistory.setAfterAmount(wallet.getCredit().subtract(creditWithDraw));
            withdrawHistory.setStatus(Constants.POINT_TRANS_STATUS.SUCCESS);
        } else {
            //TODO update withdraw history error
            withdrawHistory.setStatus(Constants.POINT_TRANS_STATUS.ERROR);
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
