package com.game.b1ingservice.usercontroller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.affiliate.AffiliateImgResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.promotion.PromotionUserRes;
import com.game.b1ingservice.service.AffiliateService;
import com.game.b1ingservice.service.PromotionService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
public class PromotionUserController {


    @Autowired
    private PromotionService promotionService;

    @Autowired
    private AffiliateService affiliateService;

    @GetMapping(value = "/promotion")
    public ResponseEntity<?> promotion(@AuthenticationPrincipal UserPrincipal principal) {
        List<PromotionUserRes> resList = promotionService.getUserPromotion(principal.getPrefix());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, resList);
    }

    @GetMapping(value = "/affiliate-image")
    public ResponseEntity<?> affiliateImg(@AuthenticationPrincipal UserPrincipal principal) {
        AffiliateImgResponse response = affiliateService.getImage(principal.getPrefix());
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, response);
    }
}
