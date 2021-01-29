package com.game.b1ingservice.specification;

import com.game.b1ingservice.payload.agent.AgentSearchRequest;
import com.game.b1ingservice.postgres.entity.AdminUser;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.specification.commons.SearchPageSpecification;
import com.game.b1ingservice.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.Date;

public class SearchAgentSpecification extends SearchPageSpecification<AgentSearchRequest, Agent> {

    public SearchAgentSpecification(AgentSearchRequest searchBody ) {
        super(searchBody);
    }

    @Override
    public Predicate toPredicate(Root<Agent> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if (StringUtils.isNotEmpty(searchBody.getCompanyName())) {
            String companyName = StringUtils.trimToEmpty(searchBody.getCompanyName());
            predicates.add(
                    criteriaBuilder.like(root.<String>get("companyName"), "%" + companyName + "%")
            );
        }


        boolean parseCreateDateFrom = DateUtils.canCastDate(searchBody.getCreatedDateFrom());
        boolean parseCreateDateTo = DateUtils.canCastDate(searchBody.getCreatedDateTo());

        if (parseCreateDateFrom && parseCreateDateTo) {
            predicates.add(criteriaBuilder.between(
                    root.<Instant>get("createdDate"), DateUtils.convertStartDate(searchBody.getCreatedDateFrom()).toInstant(), DateUtils.convertEndDate(searchBody.getCreatedDateTo()).toInstant()
            ));
        } else if (parseCreateDateFrom) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Instant>get("createdDate"), DateUtils.convertStartDate(searchBody.getCreatedDateFrom()).toInstant()));
        } else if (parseCreateDateTo) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Instant>get("createdDate"),DateUtils.convertStartDate(searchBody.getCreatedDateTo()).toInstant()));
        }

        // add search prefix
        if (StringUtils.isNotEmpty(searchBody.getPrefix())){
            String prefix = StringUtils.trimToEmpty(searchBody.getPrefix());
            predicates.add(criteriaBuilder.like(root.get("prefix"),"%"+prefix+"%"));
        }


        return super.buildParallelPredicate(root, query, criteriaBuilder);
    }


}
