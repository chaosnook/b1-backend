package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.affiliate.AffiliateDTO;
import com.game.b1ingservice.payload.affiliate.AffiliateResult;
import com.game.b1ingservice.postgres.entity.WebUser;

import java.math.BigDecimal;

public interface AffiliateService {

    AffiliateResult registerAffiliate(WebUser userResp, String userAff);

    AffiliateResult earnPoint(Long userDepos, BigDecimal credit, String prefix);

    void createOrUpdateAffiliate(AffiliateDTO request, String prefix);

    AffiliateDTO getAffiliateByPrefix(String prefix);
}
