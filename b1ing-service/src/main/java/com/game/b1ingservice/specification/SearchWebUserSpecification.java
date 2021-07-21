package com.game.b1ingservice.specification;

import com.game.b1ingservice.payload.webuser.WebUserSearchRequest;
import com.game.b1ingservice.postgres.entity.Agent;
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

        Join<WebUser, Agent> agent = root.join("agent", JoinType.INNER);

        predicates.add(criteriaBuilder.equal(agent.<Long>get("id"), searchBody.getAgentId()));

        if (StringUtils.isNotEmpty(searchBody.getBankName())){
            String bankName = StringUtils.trimToEmpty(searchBody.getBankName());

            predicates.add(
                    criteriaBuilder.like(root.get("bankName"),"%"+bankName+"%"));
        }

        if (StringUtils.isNotEmpty(searchBody.getUsername())) {
            String username = StringUtils.trimToEmpty(searchBody.getUsername().toLowerCase());

                predicates.add(
                        criteriaBuilder.like(root.get("username"), "%" + username + "%")
                );
            }


        if (StringUtils.isNotEmpty(searchBody.getTel())){
            String tel = StringUtils.trimToEmpty(searchBody.getTel());

            predicates.add(
                    criteriaBuilder.like(root.get("tel"),"%"+tel+"%"));
        }

        if (StringUtils.isNotEmpty(searchBody.getAccountNumber())) {
            String accountNumber = StringUtils.trimToEmpty(searchBody.getAccountNumber());

            predicates.add(
                    criteriaBuilder.like(root.get("accountNumber"), "%" + accountNumber + "%")
            );
        }

        if (StringUtils.isNotEmpty(searchBody.getFirstName())) {
            String firstName = StringUtils.trimToEmpty(searchBody.getFirstName());

            predicates.add(
                    criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%")
            );
        }

        if (StringUtils.isNotEmpty(searchBody.getLastName())) {
            String lastName = StringUtils.trimToEmpty(searchBody.getLastName());

            predicates.add(
                    criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%")
            );
        }

        if( searchBody.getTypeUser() == 1) {
            predicates.add(
                    criteriaBuilder.greaterThan(criteriaBuilder.size(root.get("depositHistory")), 0));
        } else if ( searchBody.getTypeUser() == 2) {
            predicates.add(
                    criteriaBuilder.equal(criteriaBuilder.size(root.get("depositHistory")), 0));
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
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Instant>get("createdDate"), DateUtils.convertEndDate(searchBody.getCreatedDateTo()).toInstant()));
            }


            return super.buildParallelPredicate(root, query, criteriaBuilder);
        }
    }

