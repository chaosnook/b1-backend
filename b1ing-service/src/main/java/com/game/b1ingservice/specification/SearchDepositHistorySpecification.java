package com.game.b1ingservice.specification;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.payload.deposithistory.DepositHistorySearchRequest;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.specification.commons.SearchPageSpecification;
import com.game.b1ingservice.utils.DateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.Instant;

public class SearchDepositHistorySpecification extends SearchPageSpecification<DepositHistorySearchRequest, DepositHistory> {

    public SearchDepositHistorySpecification(DepositHistorySearchRequest searchBody) {
        super(searchBody);
    }

    @Override
    public Predicate toPredicate(Root<DepositHistory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Join<DepositHistory, Agent> agent = root.join("agent", JoinType.INNER);

        predicates.add(criteriaBuilder.equal(agent.<Long>get("id"), searchBody.getAgentId()));

        if (StringUtils.isNotEmpty(searchBody.getUsername())) {
            Join<DepositHistory, WebUser> member = root.join("user", JoinType.INNER);
            String username = StringUtils.trimToEmpty(searchBody.getUsername());
            predicates.add(criteriaBuilder.like(member.<String>get("username"), "%" + username + "%"));
        }

        if (ObjectUtils.isNotEmpty(searchBody.getAmount())) {
            BigDecimal amount = searchBody.getAmount();
            predicates.add(criteriaBuilder.equal(root.get("amount"), amount));
        }

        if (StringUtils.isNotEmpty(searchBody.getType())) {
            if("ERROR".equals(searchBody.getType())) {
                predicates.add(criteriaBuilder.isNotNull(root.get("mistakeType")));
            } else if("TRUEWALLET".equals(searchBody.getType())) {
                predicates.add(criteriaBuilder.equal(root.get("type"), "TRUEWALLET"));
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

        if (searchBody.isSummary()) {
            predicates.add(criteriaBuilder.equal(root.get("status"), Constants.DEPOSIT_STATUS.SUCCESS));
        }

        if (searchBody.getIsMainPage() != null && !searchBody.getIsMainPage()) {
            predicates.add(criteriaBuilder.notEqual(root.<String>get("status"), Constants.DEPOSIT_STATUS.BLOCK_AUTO));
            predicates.add(criteriaBuilder.notEqual(root.<String>get("status"), Constants.DEPOSIT_STATUS.NOT_SURE));
        }

        if (StringUtils.isNotEmpty(searchBody.getStatus())) {
            String status = StringUtils.trimToEmpty(searchBody.getStatus());
            predicates.add(criteriaBuilder.equal(root.<String>get("status"), status));
        }

        return super.buildParallelPredicate(root, query, criteriaBuilder);
    }
}
