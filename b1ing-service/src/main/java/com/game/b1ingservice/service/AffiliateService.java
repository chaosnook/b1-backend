package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.affiliate.AffiliateDTO;
import com.game.b1ingservice.payload.affiliate.AffiliateResult;

public interface AffiliateService {

    AffiliateResult registerAffiliate(Long userReg, Long userAff, String prefix);

    void createOrUpdateAffiliate(AffiliateDTO request, String prefix);

    AffiliateDTO getAffiliateByPrefix(String prefix);
}
