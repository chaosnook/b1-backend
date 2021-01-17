package com.game.b1ingservice.payload.admin;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class AdminUserResponse {
    private Long id;

    private Instant createdDate;
    private Instant updatedDate;
    private String createdBy;
    private String updatedBy;
    private Integer deleteFlag;
    private Integer version;

    private String username;
    private String tel;
    private String fullName;
    private int limitFlag;
    private Integer limit;
    private int active;

    private String role;

}
