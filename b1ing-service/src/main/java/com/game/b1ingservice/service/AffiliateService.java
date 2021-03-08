package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.affiliate.AffiliateDTO;
import com.game.b1ingservice.payload.affiliate.AffiliateResult;
import com.game.b1ingservice.postgres.entity.WebUser;

public interface AffiliateService {

    AffiliateResult registerAffiliate(WebUser userResp, String userAff);

    void createOrUpdateAffiliate(AffiliateDTO request, String prefix);

    AffiliateDTO getAffiliateByPrefix(String prefix);
}
