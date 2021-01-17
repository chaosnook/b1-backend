package com.game.b1ingservice.payload.thieve;

import lombok.Data;

@Data
public class ThieveResponse {
    private Long id;
    private String name;
    private String bank_name;
    private String bank_account;
}
