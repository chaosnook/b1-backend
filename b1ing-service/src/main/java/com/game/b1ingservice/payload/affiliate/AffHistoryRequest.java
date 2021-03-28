package com.game.b1ingservice.payload.affiliate;

import com.game.b1ingservice.specification.commons.SearchPageable;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AffHistoryRequest extends SearchPageable {
    private String username;
    private String listDateFrom;
    private String listDateTo;
}
