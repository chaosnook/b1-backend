package com.game.b1ingservice.controller;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.promotion.PromotionRequest;
import com.game.b1ingservice.payload.promotion.PromotionUpdate;
import com.game.b1ingservice.service.PromotionService;
import com.game.b1ingservice.utils.ResponseHelper;
import com.game.b1ingservice.validator.promotion.PromotionUpdateValidator;
import com.game.b1ingservice.validator.promotion.PromotionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/admin")
@Slf4j
public class PromotionController {

    @Autowired
    PromotionValidator promotionValidator;

    @Autowired
    PromotionUpdateValidator promotionUpdateValidator;

    @Autowired
    PromotionService promotionService;

    //insert promotion
    @PostMapping(value = "/promotion")
//            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<?> insertPromotion(@RequestParam("urlImage") MultipartFile multipartFile, @RequestBody PromotionRequest promotionRequest, @AuthenticationPrincipal UserPrincipal principal) throws IOException {
        public ResponseEntity<?> insertPromotion(@RequestBody PromotionRequest promotionRequest, @AuthenticationPrincipal UserPrincipal principal) {

            promotionValidator.validate(promotionRequest);
//        promotionService.insertPromotion(multipartFile, promotionRequest, principal);
        promotionService.insertPromotion(promotionRequest, principal);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    //get promotion
    @GetMapping(value = "/promotion",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getPromotion(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, promotionService.getPromotion(principal));

    }

    //update promotion
    @PutMapping(value = "promotion/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> updatePromotion(@PathVariable Long id, @RequestBody PromotionUpdate promotionUpdate, @AuthenticationPrincipal UserPrincipal principal) {
        promotionUpdateValidator.validate(promotionUpdate, id);
        promotionService.updatePromotion(id, promotionUpdate, principal);
        return  ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

    //delete promotion
    @DeleteMapping(value = "promotion/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public  ResponseEntity<?> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseHelper.success(Constants.MESSAGE.MSG_00000.msg);
    }

}
