package com.game.b1ingservice.service;

import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.jdbc.dto.PointHistoryDTO;

public interface PointHistoryService {

    PointHistoryDTO create(PointHistoryDTO pointHistoryDTO, WebUser webUser);

    void updateStatus(PointHistoryDTO pointHistoryDTO);
}
