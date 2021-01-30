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

        if (StringUtils.isNotEmpty(searchBody.getBankCode())){
            String bankCode = StringUtils.trimToEmpty(searchBody.getBankCode());

            predicates.add(
                    criteriaBuilder.like(root.get("bankCode"),"%"+bankCode+"%"));
        }

        if (StringUtils.isNotEmpty(searchBody.getUserName())) {
            String userName = StringUtils.trimToEmpty(searchBody.getUserName());

                predicates.add(
                        criteriaBuilder.like(root.get("userName"), "%" + userName + "%")
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

