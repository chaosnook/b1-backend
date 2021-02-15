package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.postgres.entity.PointHistory;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.jdbc.dto.PointHistoryDTO;
import com.game.b1ingservice.postgres.repository.PointHistoryRepository;
import com.game.b1ingservice.service.PointHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PointHistoryServiceImpl implements PointHistoryService {


    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Override
    public PointHistoryDTO create(PointHistoryDTO pointHistoryDTO, WebUser webUser) {
        PointHistory history = new PointHistory();
        history.setUser(webUser);
        history.setAmount(pointHistoryDTO.getAmount());
        history.setAfterAmount(pointHistoryDTO.getAfterAmount());
        history.setBeforeAmount(pointHistoryDTO.getBeforeAmount());
        history.setStatus(Constants.POINT_TRANS_STATUS.PENDING);
        history.setType(pointHistoryDTO.getType());
        pointHistoryRepository.save(history);

        pointHistoryDTO.setId(history.getId());
        return pointHistoryDTO;
    }

    @Override
    public void updateStatus(PointHistoryDTO pointHistoryDTO) {
        Optional<PointHistory> historyOpt = pointHistoryRepository.findById(pointHistoryDTO.getId());
        if (historyOpt.isPresent()) {
            PointHistory history = historyOpt.get();
            history.setAfterAmount(pointHistoryDTO.getAfterAmount());
            history.setStatus(pointHistoryDTO.getStatus());
            history.setReason(pointHistoryDTO.getReason());
            pointHistoryRepository.save(history);
        }
    }
}
