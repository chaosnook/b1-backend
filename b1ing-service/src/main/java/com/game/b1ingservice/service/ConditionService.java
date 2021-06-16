package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.condition.ConditionRequest;
import com.game.b1ingservice.payload.condition.ConditionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConditionService {
    void insertCondition(ConditionRequest conditionRequest, UserPrincipal principal);

    List<ConditionResponse> getCondition(UserPrincipal principal);

    void updateCondition(Long id, ConditionRequest conditionRequest, UserPrincipal principal);

    void deleteCondition(Long id);
}
