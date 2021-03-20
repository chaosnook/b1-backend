package com.game.b1ingservice.payload.point;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class PointTransRequest {
    private BigDecimal point;

}
