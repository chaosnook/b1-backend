package com.game.b1ingservice.payload.agent;

import com.game.b1ingservice.specification.commons.SearchPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
public class AgentSearchRequest extends SearchPageable {
//    find by agent data
    private String companyName;
    private String createdDateFrom;
    private String createdDateTo;

}
