package com.game.b1ingservice.payload.agent;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class AgentRequest {
    private Long id;

    private Instant createdDate;
    private Instant updatedDate;
    private String createdBy;
    private String updatedBy;
    private Integer deleteFlag;
    private Integer version;

    private String background;
    private String companyName;
    private String lineId;
    private String lineToken;
    private String lineTokenWithdraw;
    private String logo;
    private String prefix;
    private String website;

    private Map<String, Object> config;
}
