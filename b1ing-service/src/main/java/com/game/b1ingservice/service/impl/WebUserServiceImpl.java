package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.payload.webuser.WebUserResponse;
import com.game.b1ingservice.payload.webuser.WebUserUpdate;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.service.WebUserService;
import com.game.b1ingservice.utils.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WebUserServiceImpl implements WebUserService {

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EntityManager em;

    @Override
    public ResponseEntity<?> createUser(WebUserRequest req) {

        String tel = req.getTel();
        String prefix = "BNG";
        String username = prefix + tel.substring(3, tel.length()-1);

        WebUser user = new WebUser();
        user.setUsername(username);
        user.setTel(req.getTel());
        user.setPassword(bCryptPasswordEncoder.encode(req.getPassword()));
        user.setBankName(req.getBankName());
        user.setAccountNumber(req.getAccountNumber());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setLine(req.getLine());
        user.setIsBonus(req.getIsBonus());

        webUserRepository.save(user);

        WebUserResponse resp = new WebUserResponse();
        resp.setUsername(username);
        resp.setPassword(req.getPassword());

        return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, resp);
    }

    @Override
    public void updateUser(Long id, WebUserUpdate req) {
        Optional<WebUser> opt = webUserRepository.findById(id);
        if(opt.isPresent()) {
            WebUser user = opt.get();
            user.setBankName(req.getBankName());
            user.setAccountNumber(req.getAccountNumber());
            user.setFirstName(req.getFirstName());
            user.setLastName(req.getLastName());
            user.setLine(req.getLine());
            user.setIsBonus(req.getIsBonus());

            webUserRepository.save(user);
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }

    @Override
    public List<WebUserResponse> getUserList() {
        return webUserRepository.findAll().stream().map(converter).collect(Collectors.toList());
    }

    Function<WebUser, WebUserResponse> converter = users -> {
        WebUserResponse webUserResponse = new WebUserResponse();
        webUserResponse.setId(users.getId());
        webUserResponse.setUsername(users.getUsername());
        webUserResponse.setPassword(users.getPassword());
        webUserResponse.setTel(users.getTel());
        webUserResponse.setBankName(users.getBank_name());
        webUserResponse.setAccountNumber(users.getAccount_number());
        webUserResponse.setFirstName(users.getFirst_name());
        webUserResponse.setLastName(users.getLast_name());
        webUserResponse.setLine(users.getLine());
        webUserResponse.setIsBonus(users.getIs_bonus());

        webUserResponse.setCreatedDate(users.getCreatedDate());
        webUserResponse.setUpdatedDate(users.getUpdatedDate());
        webUserResponse.setCreatedBy(users.getAudit().getCreatedBy());
        webUserResponse.setUpdatedBy(users.getAudit().getUpdatedBy());
        webUserResponse.setDeleteFlag(users.getDeleteFlag());
        webUserResponse.setVersion(users.getVersion());

        return webUserResponse;
    };


    public List<WebUser> findUserByUserName(String userName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WebUser> cq = cb.createQuery(WebUser.class);

        Root<WebUser> webUser = cq.from(WebUser.class);
        Predicate userNamePredicate = cb.equal(webUser.get("userName"), userName);
        cq.where(userNamePredicate);

        TypedQuery<WebUser> query = em.createQuery(cq);
        return query.getResultList();
    }
}
