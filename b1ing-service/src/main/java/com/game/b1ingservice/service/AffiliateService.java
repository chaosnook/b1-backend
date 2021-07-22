package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.affiliate.AffiliateDTO;
import com.game.b1ingservice.payload.affiliate.AffiliateImgResponse;
import com.game.b1ingservice.payload.affiliate.AffiliateResult;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.WebUser;

import java.math.BigDecimal;

public interface AffiliateService {

    AffiliateResult registerAffiliate(WebUser userResp, String userAff, Agent agent);

    AffiliateResult earnPoint(Long userDepos, BigDecimal credit, Long agentId);

    void createOrUpdateAffiliate(AffiliateDTO request, Long agentId);

    AffiliateDTO getAffiliateByPrefix(Long agentId);

    AffiliateImgResponse getImage(Long agentId);
}
