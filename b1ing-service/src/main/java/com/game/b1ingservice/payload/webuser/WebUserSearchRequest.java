package com.game.b1ingservice.payload.webuser;

import com.game.b1ingservice.specification.commons.SearchPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class WebUserSearchRequest extends SearchPageable {

    private String bankCode;
    private String userName;
    private String accountNumber;
    private String tel;
    private String firstName;
    private String lastName;
    
    private String createdDateFrom;
    private String createdDateTo;
}
