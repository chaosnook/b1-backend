package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.promotion.*;
import com.game.b1ingservice.postgres.entity.Promotion;
import com.game.b1ingservice.postgres.entity.PromotionHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PromotionService {
    void insertPromotion(PromotionRequest promotionRequest, UserPrincipal principal);

    List<PromotionResponse> getPromotion(UserPrincipal principal);

    void updatePromotion(Long id, PromotionUpdate promotionUpdate, UserPrincipal principal);

    void deletePromotion(Long id);

    List<PromotionUserRes> getUserPromotion(Long agentId);

    List<Promotion> getEffectivePromotion(PromotionEffectiveRequest request, Long agentId);

    PromotionHistory calculatePromotionBonus(Promotion promotion, PromotionEffectiveRequest request);
}
