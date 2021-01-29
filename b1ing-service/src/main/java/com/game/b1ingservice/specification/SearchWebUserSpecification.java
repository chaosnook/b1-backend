package com.game.b1ingservice.specification;

import com.game.b1ingservice.payload.webuser.WebUserSearchRequest;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.specification.commons.SearchPageSpecification;
import com.game.b1ingservice.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.time.Instant;


public class SearchWebUserSpecification extends SearchPageSpecification<WebUserSearchRequest, WebUser> {

    public SearchWebUserSpecification(WebUserSearchRequest searchBody) {
        super(searchBody);
    }

    @Override
    public Predicate toPredicate(Root<WebUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if (StringUtils.isEmpty(searchBody.getAccountNumber())) {
            String accountNumber = StringUtils.trimToEmpty(searchBody.getAccountNumber());

                predicates.add(
                        criteriaBuilder.like(root.get("accountNumber"), "%" + accountNumber + "%")
                );
            }

            boolean parseCreateDateFrom = DateUtils.canCastDate(searchBody.getCreatedDateFrom());
            boolean parseCreateDateTo = DateUtils.canCastDate(searchBody.getCreatedDateTo());

            if (parseCreateDateFrom && parseCreateDateTo) {
                predicates.add(criteriaBuilder.between(root.<Instant>get("createdDate")
                        , DateUtils.convertStartDate(searchBody.getCreatedDateFrom()).toInstant()
                        , DateUtils.convertEndDate(searchBody.getCreatedDateTo()).toInstant()));
            } else if (parseCreateDateFrom) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Instant>get("createdDate"), DateUtils.convertStartDate(searchBody.getCreatedDateFrom()).toInstant()));
            } else if (parseCreateDateTo) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Instant>get("createdDate"), DateUtils.convertStartDate(searchBody.getCreatedDateTo()).toInstant()));
            }

            return super.buildParallelPredicate(root, query, criteriaBuilder);
        }
    }

