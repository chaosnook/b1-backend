package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.affiliate.AffiliateDTO;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.service.AffiliateService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.affiliate.AffiliateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AffiliateController {

    @Autowired
    private AffiliateService affiliateService;

    @Autowired
    private AffiliateValidator affiliateValidator;

    @GetMapping(value = "/affiliate",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getAffiliate( @AuthenticationPrincipal UserPrincipal principal) {
        AffiliateDTO aff = affiliateService.getAffiliateByPrefix(principal.getPrefix());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, aff);
    }

    @PostMapping(value = "/affiliate",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> createAffiliate(@RequestBody AffiliateDTO request, @AuthenticationPrincipal UserPrincipal principal) {
        affiliateValidator.validate(request, principal);
        affiliateService.createOrUpdateAffiliate(request, principal.getPrefix());
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    @PutMapping(value = "/affiliate",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> updateAffiliate(@RequestBody AffiliateDTO request, @AuthenticationPrincipal UserPrincipal principal) {
        affiliateService.createOrUpdateAffiliate(request, principal.getPrefix());
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }


}
