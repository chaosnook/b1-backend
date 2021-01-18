package com.game.b1ingservice.payload.role;

import lombok.Data;

@Data
public class RoleUpdateRequest {
    private Long id;
    private String roleCode;
    private String description;
}
