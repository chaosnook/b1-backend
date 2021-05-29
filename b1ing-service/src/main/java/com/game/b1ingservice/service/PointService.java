package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.point.PointTransRequest;
import com.game.b1ingservice.payload.point.PointTransResponse;

import java.math.BigDecimal;

public interface PointService {

    PointTransResponse pointTransfer(PointTransRequest transRequest, String username, String prefix);

    PointTransResponse earnPoint(BigDecimal point, Long depositUser, Long userId, String prefix, BigDecimal maxWallet);
}
