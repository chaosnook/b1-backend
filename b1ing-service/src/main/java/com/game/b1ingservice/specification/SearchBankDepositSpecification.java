package com.game.b1ingservice.specification;

import com.game.b1ingservice.payload.bankdeposit.BankDepositRequest;
import com.game.b1ingservice.postgres.entity.Bank;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.specification.commons.SearchPageSpecification;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;

public class SearchBankDepositSpecification extends SearchPageSpecification<BankDepositRequest, Wallet> {

    public SearchBankDepositSpecification(BankDepositRequest searchBody) {
        super(searchBody);

    }

    @Override
    public Predicate toPredicate(Root<Wallet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Join<Wallet, WebUser> member = root.join("user", JoinType.INNER);
        Join<Wallet, Bank> bank = root.join("bank", JoinType.INNER);

        if (StringUtils.isNotEmpty(searchBody.getUsername())) {
            String username = StringUtils.trimToEmpty(searchBody.getUsername());
            predicates.add(
                    criteriaBuilder.like(member.<String>get("username"), "%" + username + "%")
            );
        }

        if (StringUtils.isNotEmpty(searchBody.getBankCode())) {
            predicates.add(
                    criteriaBuilder.equal(bank.<Integer>get("bankCode"), searchBody.getBankCode())
            );
        }

        if (searchBody.getBankGroup() != null) {
            predicates.add(
                    criteriaBuilder.equal(bank.<Integer>get("bankGroup"), searchBody.getBankGroup())
            );
        }

        if (searchBody.getBankOrder() != null) {
            predicates.add(
                    criteriaBuilder.equal(bank.<Integer>get("bankOrder"), searchBody.getBankOrder())
            );
        }

        predicates.add(
                criteriaBuilder.equal(bank.<String>get("prefix"), searchBody.getPrefix())
        );

        return super.buildParallelPredicate(root, query, criteriaBuilder);
    }

}
