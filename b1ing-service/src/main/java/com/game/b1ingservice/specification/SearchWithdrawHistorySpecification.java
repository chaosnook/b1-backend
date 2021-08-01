package com.game.b1ingservice.specification;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.withdrawhistory.WithdrawHistorySearchRequest;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.entity.WithdrawHistory;
import com.game.b1ingservice.specification.commons.SearchPageSpecification;
import com.game.b1ingservice.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.time.Instant;

public class SearchWithdrawHistorySpecification extends SearchPageSpecification<WithdrawHistorySearchRequest, WithdrawHistory> {

    public SearchWithdrawHistorySpecification(WithdrawHistorySearchRequest searchBody) {
        super(searchBody);
    }

    @Override
    public Predicate toPredicate(Root<WithdrawHistory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Join<WithdrawHistory, WebUser> member = root.join("user", JoinType.INNER);

        Join<WithdrawHistory, Agent> agent = root.join("agent", JoinType.INNER);

        predicates.add(criteriaBuilder.equal(agent.<Long>get("id"), searchBody.getAgentId()));

        if (StringUtils.isNotEmpty(searchBody.getUsername())) {
            String username = StringUtils.trimToEmpty(searchBody.getUsername().toLowerCase());
            predicates.add(criteriaBuilder.like(member.<String>get("username"), "%" + username + "%"));
        }

        if (StringUtils.isNotEmpty(searchBody.getStatus())) {
            String status = StringUtils.trimToEmpty(searchBody.getStatus());
            predicates.add(criteriaBuilder.equal(root.<String>get("status"), status));
        }

        if (searchBody.getIsMainPage() != null && !searchBody.getIsMainPage()) {
            predicates.add(criteriaBuilder.notEqual(root.<String>get("status"), Constants.WITHDRAW_STATUS.BLOCK_AUTO));
        }

        predicates.add(criteriaBuilder.isNull(root.<String>get("mistakeType")));

        if (StringUtils.isNotEmpty(searchBody.getIsAuto())) {
            Boolean isAuto = searchBody.getIsAuto().equals("1");
            if (isAuto) {
                predicates.add(criteriaBuilder.equal(root.<Boolean>get("isAuto"), isAuto));
            } else {
                predicates.add(criteriaBuilder.notEqual(root.<Boolean>get("isAuto"), true));
            }
        }

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

        return super.buildParallelPredicate(root, query, criteriaBuilder);
    }
}
