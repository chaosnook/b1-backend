package com.game.b1ingservice.specification;

import com.game.b1ingservice.payload.promotion.PromotionHistorySearchRequest;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.PromotionHistory;
import com.game.b1ingservice.specification.commons.SearchPageSpecification;
import com.game.b1ingservice.utils.DateUtils;

import javax.persistence.criteria.*;
import java.time.Instant;

public class SearchPromotionHistorySpecification extends SearchPageSpecification<PromotionHistorySearchRequest, PromotionHistory> {

    public SearchPromotionHistorySpecification(PromotionHistorySearchRequest searchBody) {super(searchBody); }

    @Override
    public Predicate toPredicate(Root<PromotionHistory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Join<PromotionHistory, Agent> agent = root.join("agent", JoinType.INNER);

        boolean parseCreateDateFrom = DateUtils.canCastDateTime(searchBody.getCreatedDateFrom());
        boolean parseCreateDateTo = DateUtils.canCastDateTime(searchBody.getCreatedDateTo());

        if (parseCreateDateFrom && parseCreateDateTo) {
            predicates.add(criteriaBuilder.between(root.<Instant>get("createdDate")
                    , DateUtils.convertStartDateTime(searchBody.getCreatedDateFrom()).toInstant()
                    , DateUtils.convertEndDateTime(searchBody.getCreatedDateTo()).toInstant()));
        } else if (parseCreateDateFrom) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Instant>get("createdDate"), DateUtils.convertStartDateTime(searchBody.getCreatedDateFrom()).toInstant()));
        } else if (parseCreateDateTo) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Instant>get("createdDate"), DateUtils.convertEndDateTime(searchBody.getCreatedDateTo()).toInstant()));
        }


        predicates.add(criteriaBuilder.equal(agent.<Long>get("id"), searchBody.getAgentId()));

        return super.buildParallelPredicate(root, query, criteriaBuilder);

    }
}
