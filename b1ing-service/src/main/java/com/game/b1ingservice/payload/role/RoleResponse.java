package com.game.b1ingservice.payload.role;

import lombok.Data;

import java.time.Instant;

@Data
public class RoleResponse {
    private Long id;
    private String roleCode;
    private String description;
    private Integer version;
    private Instant createdDate;
    private Instant updatedDate;
    private String createdBy;
    private String updatedBy;

}
