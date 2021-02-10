package com.game.b1ingservice.payload.thieve;

import com.game.b1ingservice.specification.commons.SearchPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class ThieveSearchRequest extends SearchPageable {
    private String name;
    private String bankName;
    private String bankAccount;
}
