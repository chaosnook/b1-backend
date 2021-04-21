package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.condition.ConditionResponse;
import com.game.b1ingservice.payload.promotion.*;
import com.game.b1ingservice.postgres.entity.*;
import com.game.b1ingservice.postgres.repository.AdminUserRepository;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.postgres.repository.ConditionRepository;
import com.game.b1ingservice.postgres.repository.PromotionRepository;
import com.game.b1ingservice.service.ConditionService;
import com.game.b1ingservice.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
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
    ConditionRepository conditionRepository;

    @Autowired
    AgentRepository agentRepository;

    @Autowired
    AdminUserRepository adminUserRepository;

    @Override
        public void insertPromotion(PromotionRequest promotionRequest, UserPrincipal principal) {

        Optional<Agent> opt = agentRepository.findByPrefix(principal.getPrefix());
        Optional<AdminUser> optAdmin = adminUserRepository.findById(principal.getId());
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
        promotion.setAgent(opt.get());
        promotion.setAdmin(optAdmin.get());

        promotion.setCondition(promotionRequest.getConditions().stream().map(conditionRequest -> {
            Condition condition = new Condition();
            condition.setPromotion(promotion);
            condition.setMinTopup(conditionRequest.getMinTopup());
            condition.setMaxTopup(conditionRequest.getMaxTopup());
            condition.setBonus(conditionRequest.getBonus());
            condition.setPromotion(promotion);
            return condition;
        }).collect(Collectors.toList()));

        promotionRepository.save(promotion);

    }

    @Override
    public List<PromotionResponse> getPromotion(UserPrincipal principal) {
        return promotionRepository.findAllByOrderByIdDesc().stream().map(converter).collect(Collectors.toList());
    }

    @Override
    public void updatePromotion(Long id, PromotionUpdate promotionUpdate, UserPrincipal principal){

        Optional<Agent> opt = agentRepository.findByPrefix(principal.getPrefix());
        Optional<AdminUser> optAdmin = adminUserRepository.findById(principal.getId());
        if (!opt.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        Optional<Promotion> optionalPromotion = promotionRepository.findById(id);
        if(optionalPromotion.isPresent()) {
            Promotion promotion = optionalPromotion.get();
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
            promotion.setAgent(opt.get());
            promotion.setAdmin(optAdmin.get());



            promotion.setCondition(promotionUpdate.getConditions().stream().map(conditionUpdate -> {
                Condition condition;
                if(ObjectUtils.isEmpty(conditionUpdate.getId())) {
                    condition = new Condition();
                } else {
                    condition = conditionRepository.findById(conditionUpdate.getId()).orElse(new Condition());
                }
                condition.setPromotion(promotion);
                condition.setMinTopup(conditionUpdate.getMinTopup());
                condition.setMaxTopup(conditionUpdate.getMaxTopup());
                condition.setBonus(conditionUpdate.getBonus());
                return condition;
            }).collect(Collectors.toList()));


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

    @Override
    public List<Promotion> getEffectivePromotion(PromotionEffectiveRequest request) {
        List<Promotion> promotionList = promotionRepository.findByDateBetweenStartTimeAndEndTimeAndMaxTopupAndMinTopup(Date.from(request.getTransactionDate()),request.getAmount());
        return promotionList;
    }

    @Override
    public PromotionHistory calculatePromotionBonus(Promotion promotion, PromotionEffectiveRequest request) {
        PromotionHistory promotionHistory = new PromotionHistory();
        promotionHistory.setPromotion(promotion);
        if (ObjectUtils.isNotEmpty(request.getUser())){
            promotionHistory.setUser(request.getUser());
            promotionHistory.setAgent(request.getUser().getAgent());
        }
        promotionHistory.setTransactionId(request.getTransactionId());
        promotionHistory.setTopup(request.getAmount());
        promotionHistory.setBonus(BigDecimal.ZERO);
        promotionHistory.setTurnOver(BigDecimal.ZERO);
        promotion.getCondition().forEach(condition -> {
           if (request.getAmount().compareTo(condition.getMinTopup())>=0 && request.getAmount().compareTo(condition.getMaxTopup())<=0){
                promotionHistory.setBonus(condition.getBonus());
           }
        });

        BigDecimal turnOver = promotionHistory.getTopup().add(promotionHistory.getBonus()).multiply(promotion.getTurnOver());
        promotionHistory.setTurnOver(turnOver);
        return promotionHistory;
    }

    Function<Promotion, PromotionUserRes> userConverter = promotion -> {
        PromotionUserRes userRes = new PromotionUserRes();
        userRes.setId(promotion.getId());
        userRes.setName(promotion.getName());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/downloadFile/")
                .path(promotion.getUrlImage())
                .toUriString();

        userRes.setUrlImage(fileDownloadUri);
        return userRes;
    };


    Function<Promotion, PromotionResponse> converter = promotion -> {
        PromotionResponse promotionResponse = new PromotionResponse();
        promotionResponse.setId(promotion.getId());
        promotionResponse.setUrlImage(promotion.getUrlImage());
        promotionResponse.setName(promotion.getName());
        promotionResponse.setType(promotion.getType());
        promotionResponse.setTypeBonus(promotion.getTypeBonus());
        promotionResponse.setTypePromotion(promotion.getTypePromotion());
        promotionResponse.setMinTopup(promotion.getMinTopup().doubleValue());
        promotionResponse.setMaxTopup(promotion.getMaxTopup().doubleValue());
        promotionResponse.setMaxBonus(promotion.getMaxBonus().doubleValue());
        promotionResponse.setMaxReceiveBonus(promotion.getMaxReceiveBonus().doubleValue());
        promotionResponse.setTurnOver(promotion.getTurnOver().doubleValue());
        promotionResponse.setMaxWithdraw(promotion.getMaxWithdraw().doubleValue());
        promotionResponse.setActive(promotion.isActive());
        promotionResponse.setUrlImage(promotion.getUrlImage());
        promotionResponse.setAgentId(promotion.getAgent().getId());
        promotionResponse.setAdminId(promotion.getAdmin().getId());

        promotionResponse.setConditions(promotion.getCondition().stream().map(condition -> {
            ConditionResponse conditionResponse = new ConditionResponse();
            conditionResponse.setId(condition.getId());
            conditionResponse.setMinTopup(condition.getMinTopup());
            conditionResponse.setMaxTopup(condition.getMaxTopup());
            conditionResponse.setBonus(condition.getBonus());
            return conditionResponse;
        }).collect(Collectors.toList()));

        return promotionResponse;

    };

}

