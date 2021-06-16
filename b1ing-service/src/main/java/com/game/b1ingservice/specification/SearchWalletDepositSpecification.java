package com.game.b1ingservice.specification;

import com.game.b1ingservice.payload.walletdeposit.WalletDepositRequest;
import com.game.b1ingservice.postgres.entity.*;
import com.game.b1ingservice.specification.commons.SearchPageSpecification;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;

public class SearchWalletDepositSpecification extends SearchPageSpecification<WalletDepositRequest, Wallet> {

    public SearchWalletDepositSpecification(WalletDepositRequest searchBody) {
        super(searchBody);

    }

    @Override
    public Predicate toPredicate(Root<Wallet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {


        Join<Wallet, WebUser> member = root.join("user", JoinType.INNER);
        Join<WebUser, Agent> agent = member.join("agent", JoinType.INNER);
        Join<Wallet, TrueWallet> trueWallet = root.join("trueWallet", JoinType.INNER);

        predicates.add(criteriaBuilder.equal(agent.<Long>get("id"), searchBody.getAgentId()));

        if (StringUtils.isNotEmpty(searchBody.getUsername())) {
            String username = StringUtils.trimToEmpty(searchBody.getUsername());
            predicates.add(
                    criteriaBuilder.like(member.<String>get("username"), "%" + username + "%")
            );
        }

        if (searchBody.getTrueWalletId() != null) {
            predicates.add(
                    criteriaBuilder.equal(trueWallet.<Integer>get("id"), searchBody.getTrueWalletId())
            );
        }

        return super.buildParallelPredicate(root, query, criteriaBuilder);
    }

}
