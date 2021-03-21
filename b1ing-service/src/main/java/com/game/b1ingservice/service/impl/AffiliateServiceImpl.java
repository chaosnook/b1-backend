package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.affiliate.AffConditionRequest;
import com.game.b1ingservice.payload.affiliate.AffiliateDTO;
import com.game.b1ingservice.payload.affiliate.AffiliateImgResponse;
import com.game.b1ingservice.payload.affiliate.AffiliateResult;
import com.game.b1ingservice.payload.point.PointTransRequest;
import com.game.b1ingservice.postgres.entity.*;
import com.game.b1ingservice.postgres.repository.*;
import com.game.b1ingservice.service.AffiliateService;
import com.game.b1ingservice.service.PointHistoryService;
import com.game.b1ingservice.service.PointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Autowired
    private PointHistoryService pointHistoryService;

    @Autowired
    private PointService pointService;

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

    @Override
    public AffiliateResult earnPoint(Long userDepos, BigDecimal credit, String prefix) {
        AffiliateResult result = new AffiliateResult();
        List<AffiliateUser> users = affiliateUserRepository.findAllByUser_Id(userDepos);
        if (!users.isEmpty()) {
            Affiliate affiliate = affiliateRepository.findFirstByAgent_PrefixAndActiveTrue(prefix);
            if (affiliate != null) {

                List<AffiliateCondition> conditionList = affiliate.getCondition();

                AffiliateCondition con = null;
                AffiliateCondition conMax = null;

                //====== check condition
                for (AffiliateCondition current : conditionList) {
                    if (credit.compareTo(current.getMinTopup()) >= 0 && credit.compareTo(current.getMaxTopup()) <= 0) {
                        con = current;
                    }
                    if (conMax == null) {
                        conMax = current;
                    } else {
                        if (conMax.getMaxTopup().compareTo(current.getMaxTopup()) < 0) {
                            conMax = current;
                        }
                    }
                }

                // ============= check max
                if (con == null && conMax != null && credit.compareTo(conMax.getMaxTopup()) >= 0) {
                    con = conMax;
                }

                if (con == null) {
                    result.setStatus(false);
                    return result;
                }

                String type = affiliate.getTypeBonus();
                BigDecimal bonusNo = BigDecimal.ZERO;

                if (Constants.AFFILIATE_TYPE.FIX.equalsIgnoreCase(type)) {
                    bonusNo = con.getBonus();
                } else if (Constants.AFFILIATE_TYPE.PERCENT.equalsIgnoreCase(type)) {
                    BigDecimal perce = con.getBonus();
                    bonusNo = credit.multiply(perce.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN));
                }

                if (bonusNo.compareTo(affiliate.getMaxBonus()) > 0) {
                    bonusNo = affiliate.getMaxBonus();
                }

                for (AffiliateUser user : users) {
                    Long affUserId = user.getAffiliateUserTd();
                    boolean isForever = affiliate.isForever();
                    if (!isForever) {
                        if (pointHistoryService.checkNonForever(userDepos, affUserId) == 0) {
                            pointService.earnPoint(bonusNo.setScale(2, RoundingMode.HALF_DOWN), userDepos, affUserId, prefix);
                        }
                    } else {
                        pointService.earnPoint(bonusNo.setScale(2, RoundingMode.HALF_DOWN), userDepos, affUserId, prefix);
                    }

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
        affiliate.setMaxBonus(request.getMaxBonus().setScale(2, RoundingMode.HALF_DOWN));
        affiliate.setMaxWallet(request.getMaxWallet().setScale(2, RoundingMode.HALF_DOWN));
        affiliate.setMaxWithdraw(request.getMaxWithdraw().setScale(2, RoundingMode.HALF_DOWN));
        affiliate.setImg(request.getImg());
        affiliate.setActive(request.isActive());
        affiliate.setForever(request.isForever());
        affiliate.setAgent(agent.get());


        List<AffiliateCondition> conditionList = new ArrayList<>();
        for (AffConditionRequest affConditionRequest : request.getConditions()) {
            AffiliateCondition condition = new AffiliateCondition();
            condition.setMaxTopup(affConditionRequest.getMaxTopup().setScale(2, RoundingMode.HALF_DOWN));
            condition.setMinTopup(affConditionRequest.getMinTopup().setScale(2, RoundingMode.HALF_DOWN));
            condition.setBonus(affConditionRequest.getBonus().setScale(2, RoundingMode.HALF_DOWN));
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
            affRes.setId(affiliate.getId());
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

    @Override
    public AffiliateImgResponse getImage(String prefix) {
        AffiliateImgResponse response = new AffiliateImgResponse();
        Affiliate affiliate = affiliateRepository.findFirstByAgent_Prefix(prefix);
        if (affiliate != null) {
            response.setImage(ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/file/downloadFile/")
                    .path(affiliate.getImg())
                    .toUriString());
        }

        return response;
    }


}
