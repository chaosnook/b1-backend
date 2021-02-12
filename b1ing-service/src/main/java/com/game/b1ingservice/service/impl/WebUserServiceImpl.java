package com.game.b1ingservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.admin.LoginRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.userinfo.UserInfoResponse;
import com.game.b1ingservice.payload.userinfo.UserProfile;
import com.game.b1ingservice.payload.webuser.WebUserRequest;
import com.game.b1ingservice.payload.webuser.WebUserResponse;
import com.game.b1ingservice.payload.webuser.WebUserUpdate;
import com.game.b1ingservice.payload.webuser.*;
import com.game.b1ingservice.payload.wellet.WalletRequest;
import com.game.b1ingservice.postgres.entity.AffiliateHistory;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.jdbc.WebUserJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.dto.SummaryRegisterUser;
import com.game.b1ingservice.postgres.repository.AffiliateHistoryRepository;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.service.WalletService;
import com.game.b1ingservice.service.WebUserService;
import com.game.b1ingservice.utils.AESUtils;
import com.game.b1ingservice.utils.JwtTokenUtil;
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
import java.time.YearMonth;
import java.util.*;
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

    @Autowired
    private WebUserJdbcRepository webUserJdbcRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public UserInfoResponse createUser(WebUserRequest req, String prefix) {

        Optional<Agent> opt = agentRepository.findByPrefix(prefix);
        if (!opt.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        String tel = req.getTel();
        String username = prefix + tel.substring(1, tel.length() - 1);

        WebUser user = new WebUser();
        user.setAgent(opt.get());

        user.setUsername(username);
        user.setTel(req.getTel());
        user.setPassword(AESUtils.encrypt(req.getPassword()));
        user.setBankName(req.getBankName());
        user.setAccountNumber(req.getAccountNumber());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setLine(req.getLine());
        user.setIsBonus(req.getIsBonus());

        WebUser userResp = webUserRepository.save(user);

        if (!StringUtils.isEmpty(req.getAffiliate())) {
            insertAffiliateHistory(req.getAffiliate(), userResp);
        }

        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setCredit(BigDecimal.valueOf(0));
        walletRequest.setPoint(BigDecimal.valueOf(0));
        walletService.createWallet(walletRequest, userResp);

        WebUserResponse resp = new WebUserResponse();
        resp.setUsername(username);
        resp.setPassword(req.getPassword());

        return convert(userResp, opt.get());
    }

    private void insertAffiliateHistory(String affiliate, WebUser userResp) {

        AffiliateHistory affiliateHistory = new AffiliateHistory();
        Optional<WebUser> opt = webUserRepository.findByUsernameOrTel(affiliate, affiliate);
        if (opt.isPresent()) {
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
        if (opt.isPresent()) {
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
    public Page<WebUserResponse> findByCriteria(Specification<WebUser> specification, Pageable pageable) {
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

        String password = passwordGenerator.generateStrongPassword();

        Optional<WebUser> opt = webUserRepository.findById(id);
        if (opt.isPresent()) {

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

    @Override
    public UserInfoResponse authUser(String username, String password, LoginRequest loginRequest) {
        try {
            Optional<Agent> agent = agentRepository.findByPrefix(loginRequest.getPrefix());

            if (!agent.isPresent()) {
                throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
            }

            Optional<WebUser> opt = webUserRepository.findByUsernameAndAgent(username, agent.get());
            if (opt.isPresent() && AESUtils.decrypt(opt.get().getPassword()).equals(password)) {
                return convert(opt.get(), agent.get());
            }
            throw new ErrorMessageException(Constants.ERROR.ERR_00007);

        } catch (Exception e) {
            throw new ErrorMessageException(Constants.ERROR.ERR_00007);
        }
    }

    @Override
    public UserProfile getProfile(String username, String prefix) {
        Optional<Agent> agent = agentRepository.findByPrefix(prefix);

        if (!agent.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        Optional<WebUser> opt = webUserRepository.findByUsernameAndAgent(username, agent.get());
        return convertProfile(opt.get(), agent.get());
    }

    @Override
    public boolean verifyTel(String tel, String prefix) {
        Optional<WebUser> webUser = webUserRepository.findByTelAndAgent_Prefix(tel, prefix);
        return webUser.isPresent();
    }

    UserInfoResponse convert(WebUser webUser, Agent agent) {
        UserInfoResponse userInfo = new UserInfoResponse();
        UserProfile profile = convertProfile(webUser, agent);
        userInfo.setProfile(profile);
        userInfo.setToken(jwtTokenUtil.generateToken(mapper.convertValue(profile, Map.class), "user"));

        return userInfo;
    }

    UserProfile convertProfile(WebUser webUser, Agent agent) {
        UserProfile profile = new UserProfile();
        profile.setUserId(webUser.getId());
        profile.setAgentId(agent.getId());
        profile.setUsername(webUser.getUsername());
        profile.setBankName(webUser.getBankName());
        profile.setAccountNumber(webUser.getAccountNumber());
        profile.setTel(webUser.getTel());
        profile.setBankName(webUser.getBankName());
        profile.setFirstName(webUser.getFirstName());
        profile.setLastName(webUser.getLastName());
        profile.setVersion(webUser.getVersion());
        profile.setPrefix(agent.getPrefix());
        profile.setIsBonus(webUser.getIsBonus());
        return profile;
    }

    @Override
    public WebUserHistoryResponse registerHistory(WebUserHistoryRequest webUserHistoryRequest, UserPrincipal principal) {
        Optional<Agent> agent = agentRepository.findByPrefix(principal.getPrefix());

        if (!agent.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        WebUserHistoryResponse resObj = new WebUserHistoryResponse();
        String type = webUserHistoryRequest.getType();
        String value = webUserHistoryRequest.getValue();

        if (type.equals("day")) {

//            //// byDay
            for (int i = 0; i < 24; i++) {
                resObj.getLabels().add(i);
                resObj.getData().add(0);
            }

            List<SummaryRegisterUser> listRegisterDay = webUserJdbcRepository.summaryRegisterUsersByDay(webUserHistoryRequest, principal);

            for (SummaryRegisterUser summaryRegisterUser : listRegisterDay) {
                resObj.getData().set(summaryRegisterUser.getLabels(), summaryRegisterUser.getData());
            }


        } else if (type.equals("month")) {

            //// byMonth
            String date = value;
            String[] dateParts = date.split("-");
            String yyyy = dateParts[0];
            String mm = dateParts[1];
            int year = Integer.parseInt(yyyy);
            int month = Integer.parseInt(mm);

            YearMonth yearMonthObject = YearMonth.of(year, month);
            int daysInMonth = yearMonthObject.lengthOfMonth();

            for (int i = 1; i <= daysInMonth; i++) {
                resObj.getLabels().add(i);
                resObj.getData().add(0);
            }

            List<SummaryRegisterUser> listRegisterMonth = webUserJdbcRepository.summaryRegisterUsersByMonth(webUserHistoryRequest, principal);

            for (SummaryRegisterUser summaryRegisterUser : listRegisterMonth) {
                resObj.getData().set(summaryRegisterUser.getLabels(), summaryRegisterUser.getData());
            }


        } else if (type.equals("year")) {

            //// byYear
            for (int i = 1; i < 13; i++) {
                resObj.getLabels().add(i);
                resObj.getData().add(0);
            }

            List<SummaryRegisterUser> listRegisterYear = webUserJdbcRepository.summaryRegisterUsersByYear(webUserHistoryRequest, principal);

            for (SummaryRegisterUser summaryRegisterUser : listRegisterYear) {
                resObj.getData().set(summaryRegisterUser.getLabels(), summaryRegisterUser.getData());
            }

        }

        return resObj;

    }

}
