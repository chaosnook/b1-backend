package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.withdraw.WithDrawRequest;
import com.game.b1ingservice.payload.withdraw.WithDrawResponse;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.service.WithDrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Slf4j
@Service
public class WithDrawServiceImpl implements WithDrawService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public WithDrawResponse withdraw(WithDrawRequest withDrawRequest, String username, String prefix) {
        Wallet wallet = walletRepository.findFirstByUser_UsernameAndUser_Agent_Prefix(username, prefix);
        if (wallet == null) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00011);
        }

        WebUser webUser = wallet.getUser();
        BigDecimal creditWithDraw = withDrawRequest.getCreditWithDraw();

        //TODO create withdraw history pending

        if (wallet.getCredit().compareTo(creditWithDraw) < 0) {
            //TODO update withdraw history error
            throw new ErrorMessageException(Constants.ERROR.ERR_04002);
        }

        WithDrawResponse response = new WithDrawResponse();

        return response;
    }

   private int withDrawCredit(BigDecimal credit, Long userId) {
       try {
           //TODO call withdraw credit to AMB
//           return walletRepository.transferPointToCredit(point, point, userId);
       } catch (Exception e) {
           log.error("transferPointToCredit", e);
       }
       return 0;
   }
}
