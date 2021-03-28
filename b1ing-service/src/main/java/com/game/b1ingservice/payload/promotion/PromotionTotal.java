package com.game.b1ingservice.payload.promotion;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PromotionTotal {

    private BigDecimal summary;
    Page<PromotionSummaryHistorySearchResponse> page;
}
