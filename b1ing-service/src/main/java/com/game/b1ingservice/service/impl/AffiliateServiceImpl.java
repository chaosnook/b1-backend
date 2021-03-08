package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.affiliate.AffConditionRequest;
import com.game.b1ingservice.payload.affiliate.AffiliateDTO;
import com.game.b1ingservice.payload.affiliate.AffiliateResult;
import com.game.b1ingservice.postgres.entity.*;
import com.game.b1ingservice.postgres.repository.*;
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
    private AffiliateUserRepository affiliateUserRepository;

    @Autowired
    private WebUserRepository webUserRepository;

    @Override
    public AffiliateResult registerAffiliate(WebUser userResp, String affiliate) {
        AffiliateResult result = new AffiliateResult();
        try {
            AffiliateUser affiliateUser = new AffiliateUser();
            Optional<WebUser> opt = webUserRepository.findByUsernameOrTel(affiliate, affiliate);
            if (opt.isPresent()) {
                WebUser webUser = opt.get();
                affiliateUser.setAffiliateUserTd(webUser.getId());
            }
            affiliateUser.setAffiliate(affiliate);
            affiliateUser.setUser(userResp);
            affiliateUserRepository.save(affiliateUser);

            result.setStatus(true);
        } catch (Exception e) {
            log.error("registerAffiliate", e);
            result.setStatus(false);
        }

        return result;
    }

    public AffiliateResult earnPoint(Long userDepos, BigDecimal credit, String prefix) {
        AffiliateResult result = new AffiliateResult();
        List<AffiliateUser> users = affiliateUserRepository.findAllByUser_Id(userDepos);
        if (!users.isEmpty()) {
            Affiliate affiliate = affiliateRepository.findFirstByAgent_Prefix(prefix);
            if (affiliate != null) {

                String type = affiliate.getTypeBonus();

                List<AffiliateCondition> conditionList = affiliate.getCondition();

                affiliate.getCondition().forEach(condition -> {
                    if (credit.compareTo(condition.getMinTopup()) >= 0 && credit.compareTo(condition.getMaxTopup()) <= 0) {

                    }
                });


                for (AffiliateUser user : users) {
                    Long affUserId = user.getAffiliateUserTd();

                }
            }

        }

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
