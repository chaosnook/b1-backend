package com.game.b1ingservice.specification;

import com.game.b1ingservice.payload.bankdeposit.BankDepositRequest;
import com.game.b1ingservice.payload.deposithistory.DepositHistorySearchRequest;
import com.game.b1ingservice.postgres.entity.DepositHistory;
import com.game.b1ingservice.specification.commons.SearchPageSpecification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SearchDepositHistorySpecification extends SearchPageSpecification<DepositHistorySearchRequest, DepositHistory> {

    public SearchDepositHistorySpecification(DepositHistorySearchRequest searchBody) {
        super(searchBody);

    }

    @Override
    public Predicate toPredicate(Root<DepositHistory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
