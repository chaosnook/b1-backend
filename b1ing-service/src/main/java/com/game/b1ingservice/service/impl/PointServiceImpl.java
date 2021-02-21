package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.amb.DepositReq;
import com.game.b1ingservice.payload.point.PointTransRequest;
import com.game.b1ingservice.payload.point.PointTransResponse;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.jdbc.dto.PointHistoryDTO;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.service.AMBService;
import com.game.b1ingservice.service.PointHistoryService;
import com.game.b1ingservice.service.PointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class PointServiceImpl implements PointService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PointHistoryService pointHistoryService;

    @Autowired
    private AMBService ambService;

    @Override
    public PointTransResponse pointTransfer(PointTransRequest transRequest, String username, String prefix) {
        Wallet wallet = walletRepository.findFirstByUser_UsernameAndUser_Agent_Prefix(username, prefix);
        if (wallet == null) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00011);
        }

        WebUser webUser = wallet.getUser();
        BigDecimal point = transRequest.getPoint();

        // Create point history
        PointHistoryDTO historyDTO = new PointHistoryDTO();
        historyDTO.setStatus(Constants.POINT_TRANS_STATUS.PENDING);
        historyDTO.setType(Constants.POINT_TYPE.TRANS_CREDIT);
        historyDTO.setAmount(point);
        historyDTO.setBeforeAmount(wallet.getPoint());
        historyDTO = pointHistoryService.create(historyDTO, webUser);

        if (wallet.getPoint().compareTo(point) < 0) {
            historyDTO.setStatus(Constants.POINT_TRANS_STATUS.ERROR);
            historyDTO.setReason(Constants.ERROR.ERR_04002.msg);
            pointHistoryService.updateStatus(historyDTO);
            throw new ErrorMessageException(Constants.ERROR.ERR_04002);
        }

        int updated = this.transferPointToCredit(point, webUser.getId(), username, webUser.getAgent());

        PointTransResponse response = new PointTransResponse();
        response.setStatus(updated > 0);
        if (updated > 0) {
            historyDTO.setAfterAmount(wallet.getPoint().subtract(point));
            historyDTO.setStatus(Constants.POINT_TRANS_STATUS.SUCCESS);
        } else {
            historyDTO.setStatus(Constants.POINT_TRANS_STATUS.ERROR);
        }
        pointHistoryService.updateStatus(historyDTO);

        return response;
    }

    @Override
    public PointTransResponse earnPoint(PointTransRequest transRequest, String username, String prefix) {
        Wallet wallet = walletRepository.findFirstByUser_UsernameAndUser_Agent_Prefix(username, prefix);
        if (wallet == null) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00011);
        }

        WebUser webUser = wallet.getUser();
        BigDecimal point = transRequest.getPoint();

        PointHistoryDTO historyDTO = new PointHistoryDTO();
        historyDTO.setStatus(Constants.POINT_TRANS_STATUS.PENDING);
        historyDTO.setType(Constants.POINT_TYPE.EARN_POINT);
        historyDTO.setAmount(point);
        historyDTO.setBeforeAmount(wallet.getPoint());
        historyDTO = pointHistoryService.create(historyDTO, webUser);

        if (wallet.getPoint().compareTo(point) < 0) {
            historyDTO.setStatus(Constants.POINT_TRANS_STATUS.ERROR);
            historyDTO.setReason(Constants.ERROR.ERR_04002.msg);
            pointHistoryService.updateStatus(historyDTO);
            throw new ErrorMessageException(Constants.ERROR.ERR_04002);
        }

        int updated = this.earnPoint(point, webUser.getId());

        PointTransResponse response = new PointTransResponse();
        response.setStatus(updated > 0);
        if (updated > 0) {
            historyDTO.setAfterAmount(wallet.getPoint().add(point));
            historyDTO.setStatus(Constants.POINT_TRANS_STATUS.SUCCESS);
        } else {
            historyDTO.setStatus(Constants.POINT_TRANS_STATUS.ERROR);
        }
        pointHistoryService.updateStatus(historyDTO);
        return null;
    }

    private int earnPoint(BigDecimal point, Long userId) {
        try {
            return walletRepository.earnPoint(point, userId);
        } catch (Exception e) {
            log.error("earnPoint", e);
        }
        return 0;
    }

    private int transferPointToCredit(BigDecimal point, Long userId, String username, Agent agent) {
        try {
            point = point.setScale(2, RoundingMode.HALF_DOWN);
            int updated =   walletRepository.transferPointToCredit(point, point, userId);
            if (updated > 0) {
                ambService.deposit(DepositReq.builder().amount(point.setScale(2, RoundingMode.HALF_DOWN).toPlainString()).build() , username, agent);
            }
            return updated;
        } catch (Exception e) {
            log.error("transferPointToCredit", e);
        }
        return 0;
    }
}
