package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.affiliate.AffConditionRequest;
import com.game.b1ingservice.payload.affiliate.AffiliateDTO;
import com.game.b1ingservice.payload.affiliate.AffiliateResult;
import com.game.b1ingservice.postgres.entity.Affiliate;
import com.game.b1ingservice.postgres.entity.AffiliateCondition;
import com.game.b1ingservice.postgres.entity.AffiliateHistory;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.repository.AffiliateConditionRepository;
import com.game.b1ingservice.postgres.repository.AffiliateHistoryRepository;
import com.game.b1ingservice.postgres.repository.AffiliateRepository;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.service.AffiliateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AffiliateServiceImpl implements AffiliateService {

    @Autowired
    private AffiliateRepository affiliateRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AffiliateConditionRepository affiliateConditionRepository;

    @Autowired
    private AffiliateHistoryRepository affiliateHistoryRepository;

    @Override
    public AffiliateResult registerAffiliate(Long userReg, Long userAff, String prefix) {
        AffiliateResult result = new AffiliateResult();
//        Optional<AffiliateHistory> affiliateHistory =  affiliateHistoryRepository.findFirstByAndUser_Id(userReg);
//        if (affiliateHistory.isPresent()) {
//            AffiliateHistory history = new AffiliateHistory();
//        }
        return result;
    }

    @Transactional
    @Override
    public void createOrUpdateAffiliate(AffiliateDTO request, String prefix) {

        Optional<Agent> agent = agentRepository.findByPrefix(prefix);
        if (!agent.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        Affiliate affiliate;

        if (request.getId() != null) {
            affiliate = affiliateRepository.findFirstByAgent(agent.get());
            if (affiliate == null || !affiliate.getId().equals(request.getId())) {
                throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
            }
            affiliate.setVersion(request.getVersion());

            affiliateConditionRepository.deleteAffiliateConditionByAffiliate_Id(affiliate.getId());
        } else {
            affiliate = new Affiliate();
        }

        affiliate.setTypeBonus(request.getTypeBonus());
        affiliate.setMaxBonus(request.getMaxBonus().setScale(2, BigDecimal.ROUND_HALF_UP));
        affiliate.setMaxWallet(request.getMaxWallet().setScale(2, BigDecimal.ROUND_HALF_UP));
        affiliate.setMaxWithdraw(request.getMaxWithdraw().setScale(2, BigDecimal.ROUND_HALF_UP));
        affiliate.setImg(request.getImg());
        affiliate.setActive(request.isActive());
        affiliate.setForever(request.isForever());
        affiliate.setAgent(agent.get());


        List<AffiliateCondition> conditionList = new ArrayList<>();
        for (AffConditionRequest affConditionRequest : request.getConditions()) {
            AffiliateCondition condition = new AffiliateCondition();
            condition.setMaxTopup(affConditionRequest.getMaxTopup().setScale(2, BigDecimal.ROUND_HALF_UP));
            condition.setMinTopup(affConditionRequest.getMinTopup().setScale(2, BigDecimal.ROUND_HALF_UP));
            condition.setBonus(affConditionRequest.getBonus().setScale(2, BigDecimal.ROUND_HALF_UP));
            condition.setAffiliate(affiliate);
            conditionList.add(condition);
        }

        affiliate.setCondition(conditionList);

        affiliateRepository.save(affiliate);
    }

    @Override
    public AffiliateDTO getAffiliateByPrefix(String prefix) {
        Affiliate affiliate = affiliateRepository.findFirstByAgent_Prefix(prefix);
        AffiliateDTO affRes = new AffiliateDTO();
        if (affiliate != null) {
            affRes.setMaxBonus(affiliate.getMaxBonus());
            affRes.setMaxWallet(affiliate.getMaxWallet());
            affRes.setMaxWithdraw(affiliate.getMaxWithdraw());
            affRes.setImg(affiliate.getImg());
            affRes.setTypeBonus(affiliate.getTypeBonus());
            affRes.setActive(affiliate.isActive());
            affRes.setForever(affiliate.isForever());
            affRes.setVersion(affiliate.getVersion());

            if (affiliate.getCondition() != null) {
                List<AffConditionRequest> conditions = new ArrayList<>();
                for (AffiliateCondition condition : affiliate.getCondition()) {
                    AffConditionRequest affConRequest = new AffConditionRequest();
                    affConRequest.setBonus(condition.getBonus());
                    affConRequest.setMaxTopup(condition.getMaxTopup());
                    affConRequest.setMinTopup(condition.getMinTopup());
                    conditions.add(affConRequest);
                }

                affRes.setConditions(conditions);
            }
        }
        return affRes;
    }
}
