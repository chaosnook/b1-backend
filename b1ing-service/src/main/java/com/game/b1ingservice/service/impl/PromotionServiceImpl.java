package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.promotion.PromotionRequest;
import com.game.b1ingservice.payload.promotion.PromotionResponse;
import com.game.b1ingservice.payload.promotion.PromotionUpdate;
import com.game.b1ingservice.payload.promotion.PromotionUserRes;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.Promotion;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.postgres.repository.PromotionRepository;
import com.game.b1ingservice.service.ConditionService;
import com.game.b1ingservice.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    ConditionService conditionService;

    @Autowired
    AgentRepository agentRepository;

    @Override
        public void insertPromotion(PromotionRequest promotionRequest, UserPrincipal principal) {

        Optional<Agent> opt = agentRepository.findByPrefix(principal.getPrefix());
        if (!opt.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        Promotion promotion = new Promotion();

        promotion.setName(promotionRequest.getName());
        promotion.setTypeBonus(promotionRequest.getTypeBonus());
        promotion.setTypePromotion(promotionRequest.getTypePromotion());
        promotion.setMaxBonus(promotionRequest.getMaxBonus());
        promotion.setMinTopup(promotionRequest.getMinTopup());
        promotion.setMaxTopup(promotionRequest.getMaxTopup());
        promotion.setMaxReceiveBonus(promotionRequest.getMaxReceiveBonus());
        promotion.setTurnOver(promotionRequest.getTurnOver());
        promotion.setMaxWithdraw(promotionRequest.getMaxWithdraw());
        promotion.setActive(promotionRequest.isActive());
        promotion.setUrlImage(promotionRequest.getUrlImage());

        promotionRepository.save(promotion);

//        ConditionRequest conditionRequest = new ConditionRequest();
//        conditionRequest.setMinTopup(promotionRequest.getMinTopup());
//        conditionRequest.setMaxTopup(promotionRequest.getMaxTopup());
//        conditionRequest.setBonus(promotionRequest.getMaxReceiveBonus());
//        conditionService.insertCondition(conditionRequest, principal);

    }

    @Override
    public List<PromotionResponse> getPromotion(UserPrincipal principal) {
        return promotionRepository.findAll().stream().map(converter).collect(Collectors.toList());
    }

    @Override
    public void updatePromotion(Long id, PromotionUpdate promotionUpdate, UserPrincipal principal){
        Optional<Promotion> opt = promotionRepository.findById(id);
        if(opt.isPresent()) {
            Promotion promotion = opt.get();
            promotion.setName(promotionUpdate.getName());
            promotion.setTypeBonus(promotionUpdate.getTypeBonus());
            promotion.setTypePromotion(promotionUpdate.getTypePromotion());
            promotion.setMaxBonus(promotionUpdate.getMaxBonus());
            promotion.setMinTopup(promotionUpdate.getMinTopup());
            promotion.setMaxTopup(promotionUpdate.getMaxTopup());
            promotion.setTurnOver(promotionUpdate.getTurnOver());
            promotion.setMaxWithdraw(promotionUpdate.getMaxWithdraw());
            promotion.setActive(promotionUpdate.isActive());
            promotion.setUrlImage(promotionUpdate.getUrlImage());

            promotionRepository.save(promotion);

        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }

    @Override
    public void deletePromotion(Long id) {
        Optional<Promotion> opt = promotionRepository.findById(id);
        if (opt.isPresent()) {
            Promotion promotion = opt.get();
            promotion.setDeleteFlag(1);
            promotionRepository.save(promotion);

        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }

    @Override
    public List<PromotionUserRes> getUserPromotion(String prefix) {
        return promotionRepository.findAllByAgent_PrefixAndActive(prefix , true).stream().map(userConverter).collect(Collectors.toList());
    }

    Function<Promotion, PromotionUserRes> userConverter = promotion -> {
        PromotionUserRes userRes = new PromotionUserRes();
        userRes.setId(promotion.getId());
        userRes.setName(promotion.getName());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/downloadFile/")
                .path(promotion.getName())
                .toUriString();

        userRes.setUrlImage(fileDownloadUri);
        return userRes;
    };


    Function<Promotion, PromotionResponse> converter = promotion -> {
        PromotionResponse promotionResponse = new PromotionResponse();
        promotionResponse.setId(promotion.getId());
        promotionResponse.setUrlImage(promotion.getUrlImage());
        promotionResponse.setName(promotion.getName());
        promotionResponse.setTypePromotion(promotion.getTypePromotion());
        promotionResponse.setMinTopup(promotion.getMinTopup());
        promotionResponse.setMaxTopup(promotion.getMaxTopup());
        promotionResponse.setMaxBonus(promotion.getMaxBonus());
        promotionResponse.setTurnOver(promotion.getTurnOver());
        promotionResponse.setMaxWithdraw(promotion.getMaxWithdraw());
        promotionResponse.setActive(promotion.isActive());

        Map<String, Object> configMap = new HashMap<>();


        return promotionResponse;

    };


}

