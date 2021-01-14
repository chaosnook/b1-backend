package com.game.b1ingservice.payload.items;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddItemRequest implements Serializable {
    private String id;
    private String name;
    private String quantity;
    private String cost;
    private String sale;
}
