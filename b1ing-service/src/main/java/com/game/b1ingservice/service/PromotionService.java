package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.promotion.PromotionRequest;
import com.game.b1ingservice.payload.promotion.PromotionResponse;
import com.game.b1ingservice.payload.promotion.PromotionUpdate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Service
public interface PromotionService {
    void insertPromotion(PromotionRequest promotionRequest, UserPrincipal principal);

    List<PromotionResponse> getPromotion(UserPrincipal principal);
    void updatePromotion(Long id, PromotionUpdate promotionUpdate, UserPrincipal principal);
    void deletePromotion(Long id);
}
