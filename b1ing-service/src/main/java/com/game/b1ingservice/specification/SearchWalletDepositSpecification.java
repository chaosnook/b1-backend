package com.game.b1ingservice.specification;

import com.game.b1ingservice.payload.walletdeposit.WalletDepositRequest;
import com.game.b1ingservice.postgres.entity.TrueWallet;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.entity.WebUser;
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
        Join<Wallet, TrueWallet> trueWallet = root.join("trueWallet", JoinType.INNER);

        if (StringUtils.isNotEmpty(searchBody.getUsername())) {
            String username = StringUtils.trimToEmpty(searchBody.getUsername());
            predicates.add(
                    criteriaBuilder.like(member.<String>get("username"), "%" + username + "%")
            );
        }


        if (searchBody.getBankGroup() != null) {
            predicates.add(
                    criteriaBuilder.equal(trueWallet.<Integer>get("bankGroup"), searchBody.getBankGroup())
            );
        }

        predicates.add(
                criteriaBuilder.equal(trueWallet.<String>get("prefix"), searchBody.getPrefix())
        );

        return super.buildParallelPredicate(root, query, criteriaBuilder);
    }

}
