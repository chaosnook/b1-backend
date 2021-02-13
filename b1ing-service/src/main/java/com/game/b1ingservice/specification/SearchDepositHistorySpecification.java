package com.game.b1ingservice.specification;

import com.game.b1ingservice.payload.deposithistory.DepositHistorySearchRequest;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.specification.commons.SearchPageSpecification;
import com.game.b1ingservice.utils.DateUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Instant;

public class SearchDepositHistorySpecification extends SearchPageSpecification<DepositHistorySearchRequest, DepositHistory> {

    public SearchDepositHistorySpecification(DepositHistorySearchRequest searchBody) {
        super(searchBody);

    }

    @Override
    public Predicate toPredicate(Root<DepositHistory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        boolean parseCreateDateFrom = DateUtils.canCastDate(searchBody.getCreatedDateFrom());
        boolean parseCreateDateTo = DateUtils.canCastDate(searchBody.getCreatedDateTo());

        if (parseCreateDateFrom && parseCreateDateTo) {
            predicates.add(criteriaBuilder.between(root.<Instant>get("createdDate")
                    , DateUtils.convertStartDate(searchBody.getCreatedDateFrom()).toInstant()
                    , DateUtils.convertEndDate(searchBody.getCreatedDateTo()).toInstant()));
        } else if (parseCreateDateFrom) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Instant>get("createdDate"), DateUtils.convertStartDate(searchBody.getCreatedDateFrom()).toInstant()));
        } else if (parseCreateDateTo) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Instant>get("createdDate"), DateUtils.convertEndDate(searchBody.getCreatedDateTo()).toInstant()));
        }

        return super.buildParallelPredicate(root, query, criteriaBuilder);
    }
}
