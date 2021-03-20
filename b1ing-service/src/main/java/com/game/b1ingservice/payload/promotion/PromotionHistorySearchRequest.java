package com.game.b1ingservice.payload.promotion;

import com.game.b1ingservice.specification.commons.SearchPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class PromotionHistorySearchRequest extends SearchPageable {

    private String createdDateFrom;
    private String createdDateTo;
}
