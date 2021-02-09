package com.game.b1ingservice.specification;

import com.game.b1ingservice.payload.thieve.ThieveSearchRequest;
import com.game.b1ingservice.postgres.entity.Thieve;
import com.game.b1ingservice.specification.commons.SearchPageSpecification;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.criteria.*;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


public class SearchThieveSpecification extends SearchPageSpecification<ThieveSearchRequest, Thieve> {
    public SearchThieveSpecification(ThieveSearchRequest searchBody) {super(searchBody);}

    @Override
    public Predicate toPredicate(Root<Thieve> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if (StringUtils.isNotEmpty(searchBody.getName())) {
            String Name = StringUtils.trimToEmpty(searchBody.getName());
            predicates.add(
                    criteriaBuilder.like(root.get("name"), "%" + Name + "%")
            );
        }

        if (StringUtils.isNotEmpty(searchBody.getBankName())) {
            String bankName = StringUtils.trimToEmpty(searchBody.getBankName());

            predicates.add(
                    criteriaBuilder.like(root.get("bankName"), "%" + bankName + "%"));

        }
        if (StringUtils.isNotEmpty(searchBody.getBankAccount())) {
            String bankAccount = StringUtils.trimToEmpty(searchBody.getBankAccount());

            predicates.add(
                    criteriaBuilder.like(root.get("bankAccount"), "%" + bankAccount + "%"));

        }
    return super.buildParallelPredicate(root, query,criteriaBuilder);
    }
}
