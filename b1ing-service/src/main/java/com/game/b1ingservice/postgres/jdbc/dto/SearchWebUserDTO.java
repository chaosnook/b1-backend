package com.game.b1ingservice.postgres.jdbc.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class SearchWebUserDTO {

    private Long id;
    private String username;
    private String password;
    private String tel;
    private String bankName;
    private String accountNumber;
    private String firstName;
    private String lastName;
    private String fullName;
    private String line;
    private String isBonus;
    private String type;
    private boolean typeUser;
    private String searchValue;

    private int version;
    private Instant createdDate;
    private Instant updatedDate;
    private String createdBy;
    private String updatedBy;
    private Integer deleteFlag;
}
