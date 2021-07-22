package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.condition.ConditionRequest;
import com.game.b1ingservice.payload.condition.ConditionResponse;
import com.game.b1ingservice.postgres.entity.Condition;
import com.game.b1ingservice.postgres.entity.Promotion;
import com.game.b1ingservice.postgres.repository.ConditionRepository;
import com.game.b1ingservice.postgres.repository.PromotionRepository;
import com.game.b1ingservice.service.ConditionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConditionServiceImpl implements ConditionService {

    @Autowired
    private ConditionRepository conditionRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public void insertCondition(ConditionRequest conditionRequest, UserPrincipal principal) {

        Optional<Promotion> opt = promotionRepository.findById(conditionRequest.getPromotionId());

        if(opt.isPresent()) {

            Condition condition = new Condition();

            condition.setMinTopup(conditionRequest.getMinTopup());
            condition.setMaxTopup(conditionRequest.getMaxTopup());
            condition.setBonus(conditionRequest.getBonus());

            condition.setPromotion(opt.get());

            conditionRepository.save(condition);

        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_09011);
        }
    }

    @Override
    public List<ConditionResponse> getCondition(UserPrincipal principal) {
        return conditionRepository.findAll().stream().map(converter).collect(Collectors.toList());
    }

    @Override
    public void updateCondition(Long id, ConditionRequest conditionRequest, UserPrincipal principal) {
        Optional<Condition> opt = conditionRepository.findById(id);
        if (opt.isPresent()) {
            Condition condition = opt.get();
            condition.setMinTopup(conditionRequest.getMinTopup());
            condition.setMaxTopup(conditionRequest.getMaxTopup());
            condition.setBonus(conditionRequest.getBonus());

            conditionRepository.save(condition);
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }

    @Override
    public void deleteCondition(Long id) {
        Optional<Condition> opt = conditionRepository.findById(id);
        if (opt.isPresent()) {
            Condition condition = opt.get();
            condition.setDeleteFlag(1);

            conditionRepository.save(condition);
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }


    Function<Condition, ConditionResponse> converter = condition -> {
        ConditionResponse conditionResponse = new ConditionResponse();
        conditionResponse.setId(condition.getId());
        conditionResponse.setMinTopup(condition.getMinTopup());
        conditionResponse.setMaxTopup(condition.getMaxTopup());

        return conditionResponse;

    };
}
