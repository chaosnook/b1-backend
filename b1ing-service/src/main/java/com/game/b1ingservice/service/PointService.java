package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.point.PointTransRequest;
import com.game.b1ingservice.payload.point.PointTransResponse;

public interface PointService {

    PointTransResponse pointTransfer(PointTransRequest transRequest, String username, String password);

    PointTransResponse earnPoint(PointTransRequest transRequest, String username, String prefix);
}
