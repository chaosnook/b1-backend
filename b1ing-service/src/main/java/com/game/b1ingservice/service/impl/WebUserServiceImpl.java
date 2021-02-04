package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.payload.webuser.WebUserResponse;
import com.game.b1ingservice.payload.webuser.WebUserUpdate;
import com.game.b1ingservice.payload.wellet.WalletRequest;
import com.game.b1ingservice.postgres.entity.AffiliateHistory;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.repository.AffiliateHistoryRepository;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.service.WalletService;
import com.game.b1ingservice.service.WebUserService;
import com.game.b1ingservice.utils.PasswordGenerator;
import com.game.b1ingservice.utils.ResponseHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class WebUserServiceImpl implements WebUserService {

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private AffiliateHistoryRepository affiliateHistoryRepository;

    @Override
    public WebUserResponse createUser(WebUserRequest req, UserPrincipal principal) {
        String tel = req.getTel();
        String prefix = principal.getPrefix();
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
        Optional<Agent> opt = agentRepository.findByPrefix(principal.getPrefix());
        if (opt.isPresent()) {
            user.setAgent(opt.get());
        }
        WebUser userResp = webUserRepository.save(user);

        if(!StringUtils.isEmpty(req.getAffiliate())) {
            insertAffiliateHistory(req.getAffiliate(), userResp);
        }

        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setCredit(BigDecimal.valueOf(0));
        walletRequest.setPoint(BigDecimal.valueOf(0));
        walletService.createWallet(walletRequest, userResp);

        WebUserResponse resp = new WebUserResponse();
        resp.setUsername(username);
        resp.setPassword(req.getPassword());

        return resp;
    }

    public void insertAffiliateHistory(String affiliate, WebUser userResp) {

        AffiliateHistory affiliateHistory = new AffiliateHistory();
        Optional<WebUser> opt = webUserRepository.findByUsernameOrTel(affiliate, affiliate);
        if(opt.isPresent()) {
            WebUser affiliateUser = opt.get();
            affiliateHistory.setAffiliateUserTd(affiliateUser.getId());
        }
        affiliateHistory.setAffiliate(affiliate);
        affiliateHistory.setUser(userResp);
        affiliateHistory.setPoint(BigDecimal.valueOf(0L));

        affiliateHistoryRepository.save(affiliateHistory);
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
            public Page<WebUserResponse> findByCriteria(Specification<WebUser> specification, Pageable pageable){
        return webUserRepository.findAll(specification, pageable).map(converter);
    }

    Function<WebUser, WebUserResponse> converter = users -> {
        WebUserResponse webUserResponse = new WebUserResponse();
        webUserResponse.setId(users.getId());
        webUserResponse.setUsername(users.getUsername());
        webUserResponse.setPassword(users.getPassword());
        webUserResponse.setTel(users.getTel());
        webUserResponse.setBankName(users.getBankName());
        webUserResponse.setAccountNumber(users.getAccountNumber());
        webUserResponse.setFirstName(users.getFirstName());
        webUserResponse.setLastName(users.getLastName());
        webUserResponse.setFullName(users.getFirstName() + " " + users.getLastName());
        webUserResponse.setLine(users.getLine());
        webUserResponse.setIsBonus(users.getIsBonus());

        webUserResponse.setCreatedDate(users.getCreatedDate());
        webUserResponse.setUpdatedDate(users.getUpdatedDate());
        webUserResponse.setCreatedBy(users.getAudit().getCreatedBy());
        webUserResponse.setUpdatedBy(users.getAudit().getUpdatedBy());
        webUserResponse.setDeleteFlag(users.getDeleteFlag());
        webUserResponse.setVersion(users.getVersion());

        Map<String, Object> configMap = new HashMap<>();


        return webUserResponse;
    };

    @Override
    public ResponseEntity<?> resetPassword(Long id, WebUserUpdate webUserUpdate) {

        String password =  passwordGenerator.generateStrongPassword();

        Optional<WebUser> opt = webUserRepository.findById(id);
        if(opt.isPresent()) {

            WebUser user = opt.get();
            user.setPassword(bCryptPasswordEncoder.encode(passwordGenerator.generateStrongPassword()));
            webUserRepository.save(user);

            WebUserUpdate resp = new WebUserUpdate();
            resp.setUsername(user.getUsername());
            resp.setPassword(password);

            return ResponseHelper.successWithData(Constants.MESSAGE.MSG_00000.msg, resp);

        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }

    }

}
