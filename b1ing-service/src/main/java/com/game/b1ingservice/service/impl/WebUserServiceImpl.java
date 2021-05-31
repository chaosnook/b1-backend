package com.game.b1ingservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.admin.LoginRequest;
import com.game.b1ingservice.payload.amb.*;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.userinfo.UserInfoResponse;
import com.game.b1ingservice.payload.userinfo.UserProfile;
import com.game.b1ingservice.payload.wallet.WalletRequest;
import com.game.b1ingservice.payload.webuser.*;
import com.game.b1ingservice.postgres.entity.AffiliateUser;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.Wallet;
import com.game.b1ingservice.postgres.entity.WebUser;
import com.game.b1ingservice.postgres.jdbc.WebUserJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.dto.SummaryRegisterUser;
import com.game.b1ingservice.postgres.repository.AffiliateUserRepository;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.postgres.repository.WalletRepository;
import com.game.b1ingservice.postgres.repository.WebUserRepository;
import com.game.b1ingservice.service.AMBService;
import com.game.b1ingservice.service.AffiliateService;
import com.game.b1ingservice.service.WalletService;
import com.game.b1ingservice.service.WebUserService;
import com.game.b1ingservice.utils.AESUtils;
import com.game.b1ingservice.utils.JwtTokenUtil;
import com.game.b1ingservice.utils.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;

import static com.game.b1ingservice.commons.Constants.ERROR.ERR_04003;
import static com.game.b1ingservice.commons.Constants.ERROR.ERR_04004;

@Slf4j
@Service
public class WebUserServiceImpl implements WebUserService {

    @Autowired
    private WebUserRepository webUserRepository;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private WalletService walletService;


    @Autowired
    private AffiliateUserRepository affiliateUserRepository;

    @Autowired
    private WebUserJdbcRepository webUserJdbcRepository;

    @Autowired
    private AMBService ambService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AffiliateService affiliateService;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public UserInfoResponse createUser(WebUserRequest req, String prefix) {

        Optional<Agent> opt = agentRepository.findByPrefix(prefix);
        if (!opt.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        String tel = req.getTel();
        String username = tel.substring(3);

        WebUser user = new WebUser();
        user.setAgent(opt.get());

        user.setTel(req.getTel());
        user.setPassword(AESUtils.encrypt(req.getPassword()));
        user.setBankName(req.getBankName());
        user.setAccountNumber(req.getAccountNumber());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setLine(req.getLine());
        user.setIsBonus(req.getIsBonus());

        // Call create usr AMB
        AmbResponse<CreateUserRes> ambResponse = ambService.createUser(CreateUserReq.builder()
                .phoneNo(req.getTel())
                .memberLoginName(username)
                .memberLoginPass(req.getPassword())
                .build(), opt.get());

        log.info("create user response : {}" , ambResponse);
        if (ambResponse.getCode() != 0) {
            throw new ErrorMessageException(Constants.ERROR.ERR_99999);
        }

        // user web
        user.setUsername(prefix.concat(username));
        // user amb
        user.setUsernameAmb(ambResponse.getResult().getLoginName());

        WebUser userResp = webUserRepository.save(user);

        if (!StringUtils.isEmpty(req.getAffiliate())) {
            affiliateService.registerAffiliate(userResp, req.getAffiliate());
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

    @Override
    public void updateUser(Long id, WebUserUpdate req) {
        Optional<WebUser> optUsername = webUserRepository.findByIdNotAndUsername(id, req.getUsername());
        if (optUsername.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_01119);
        }
        Optional<WebUser> opt = webUserRepository.findById(id);
        if (opt.isPresent()) {
            WebUser user = opt.get();
            user.setUsername(req.getUsername());
            user.setBankName(req.getBankName());
            user.setAccountNumber(req.getAccountNumber());
            user.setFirstName(req.getFirstName());
            user.setLastName(req.getLastName());
            user.setTel(req.getTel());
            user.setLine(req.getLine());
            user.setIsBonus(req.getIsBonus());

            WebUser userResp = webUserRepository.save(user);

            if (!StringUtils.isEmpty(req.getAffiliate())) {
                Optional<AffiliateUser> affiliateUser = affiliateUserRepository.findFirstByAffiliateAndUser_Id(req.getAffiliate(), id);
                if (!affiliateUser.isPresent()) {
                    affiliateUserRepository.removeOleAffiliate(id);
                    affiliateService.registerAffiliate(userResp, req.getAffiliate());
                }
            }

        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01104);
        }
    }

    //ไม่ได้ใช้แล้ว

//    @Override
//    public Page<SearchWebUserDTO> searchWebUser(WebUserSearchRequest request, UserPrincipal principal) {
//
//        List<SearchWebUserDTO> listWebUser = webUserJdbcRepository.searchWebUser(request, principal);
//        Integer countWebUser = webUserJdbcRepository.countWebUser(request, principal);
//
//        List<SearchWebUserDTO> result = new ArrayList<>();
//        for (SearchWebUserDTO resp : listWebUser) {
//            SearchWebUserDTO search = new SearchWebUserDTO();
//            search.setId(resp.getId());
//            search.setUsername(resp.getUsername());
//            search.setPassword(resp.getPassword());
//            search.setTel(resp.getTel());
//            search.setBankName(resp.getBankName());
//            search.setAccountNumber(resp.getAccountNumber());
//            search.setFirstName(resp.getFirstName());
//            search.setLastName(resp.getLastName());
//            search.setFullName(resp.getFirstName() + " " + resp.getLastName());
//            search.setLine(resp.getLine());
//            search.setIsBonus(resp.getIsBonus());
//            search.setType(request.getType());
//            search.setTypeUser(request.getTypeUser());
//            search.setSearchValue(request.getSearchValue());
//
//            search.setVersion(resp.getVersion());
//            search.setCreatedDate(resp.getCreatedDate());
//            search.setUpdatedDate(resp.getUpdatedDate());
//            result.add(search);
//        }
//
//        Page<SearchWebUserDTO> searchResponse = new PageImpl<>(result, PageRequest.of(request.getPage(), request.getSize()), countWebUser);
//
//        return searchResponse;
//
//    }

    @Override
    public Page<WebUserResponse> findByCriteria(Specification<WebUser> specification, Pageable pageable, WebUserSearchRequest request) {

        Page<WebUserResponse> searchResponse = webUserRepository.findAll(specification, pageable).map(converter);

        return searchResponse;
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
        webUserResponse.setIsBonus(Boolean.valueOf(users.getIsBonus()));

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
    public WebUserResetPasswordResponse resetPassword(Long id, UserPrincipal principal) {

        String password = passwordGenerator.generateStrongPassword();

        Optional<WebUser> opt = webUserRepository.findById(id);
        if (opt.isPresent()) {

            WebUser user = opt.get();
            user.setPassword(AESUtils.encrypt(password));

            // Call reset password at AMB
            AmbResponse ambResponse = ambService.resetPassword(ResetPasswordReq.builder().password(password).build(), user.getUsernameAmb(), opt.get().getAgent());
            if (ambResponse.getCode() != 0) {
                throw new ErrorMessageException(Constants.ERROR.ERR_99999);
            }

            webUserRepository.save(user);

            WebUserResetPasswordResponse resp = new WebUserResetPasswordResponse();
            resp.setUsername(user.getUsername());
            resp.setPassword(password);

            return resp;

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

            Optional<WebUser> opt = webUserRepository.findByUsernameAndAgent(username.toLowerCase(Locale.ROOT), agent.get());
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
        return convertProfile(opt.get(), agent.get(), true);
    }

    @Override
    public void updateUserWebProfile(String username, String prefix, WebUserProfileUpdate webUserUpdate) {
        Optional<WebUser> webUser = webUserRepository.findFirstByUsernameAndAgent_Prefix(username, prefix);
        if (!webUser.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_99999);
        }

        WebUser user = webUser.get();
        user.setIsBonus(webUserUpdate.getIsBonus());
        webUserRepository.save(user);
    }

    @Override
    public WebUser getById(Long depositUser) {
        return webUserRepository.findById(depositUser).orElse(null);
    }

    @Override
    public List<WinLoseReq> getAllUser(Long agentId) {
        List<Map> objects = webUserRepository.getAllUser(agentId);
        if (objects.size() == 0) {
            return new ArrayList<>();
        }
        return mapper.convertValue(objects, new TypeReference<List<WinLoseReq>>() {
        });
    }

    @Override
    public boolean verifyTel(String tel, String prefix) {
        String username = prefix.toLowerCase(Locale.ROOT).concat(tel.substring(3));
        Optional<WebUser> webUser = webUserRepository.findFirstByUsernameAndAgent_Prefix(username, prefix);
        return webUser.isPresent();
    }

    UserInfoResponse convert(WebUser webUser, Agent agent) {
        UserInfoResponse userInfo = new UserInfoResponse();
        UserProfile profile = convertProfile(webUser, agent, false);
        userInfo.setProfile(profile);
        userInfo.setToken(jwtTokenUtil.generateToken(mapper.convertValue(profile, Map.class), "user"));
        return userInfo;
    }

    UserProfile convertProfile(WebUser webUser, Agent agent, boolean userCheck) {
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

        if (userCheck) {
            //TODO Check user can canWithDraw from turn over
            this.checkCanWithdraw(profile, webUser, agent);
        }

        return profile;
    }

    private void checkCanWithdraw(UserProfile profile, WebUser webUser, Agent agent) {
        profile.setCanWithDraw(true);
        Wallet wallet = webUser.getWallet();

        if (wallet.getCredit().compareTo(BigDecimal.ZERO) == 0) {
            profile.setCanWithDraw(false);
            profile.setWithDrawMessage(ERR_04003.msg);
        } else if (wallet.getTurnOver() == null || wallet.getTurnOver().compareTo(BigDecimal.ZERO) > 0) {
            profile.setCanWithDraw(false);
            profile.setWithDrawMessage(String.format(ERR_04004.msg, wallet.getTurnOver() == null ? "0.00" : wallet.getTurnOver()));
        }
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
            for (int i = 0; i <= 23; i++) {
                resObj.getLabels().add(i);
                resObj.getData().add(0);
            }

            List<SummaryRegisterUser> listRegisterDay = webUserJdbcRepository.summaryRegisterUsersByDay(webUserHistoryRequest, principal);

            for (SummaryRegisterUser summaryRegisterUser : listRegisterDay) {
                resObj.getData().set(summaryRegisterUser.getLabels() , summaryRegisterUser.getData());
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
                resObj.getData().set(summaryRegisterUser.getLabels() - 1, summaryRegisterUser.getData());
            }


        } else if (type.equals("year")) {

            //// byYear
            for (int i = 1; i < 13; i++) {
                resObj.getLabels().add(i);
                resObj.getData().add(0);
            }

            List<SummaryRegisterUser> listRegisterYear = webUserJdbcRepository.summaryRegisterUsersByYear(webUserHistoryRequest, principal);

            for (SummaryRegisterUser summaryRegisterUser : listRegisterYear) {
                resObj.getData().set(summaryRegisterUser.getLabels() - 1, summaryRegisterUser.getData());
            }

        }

        return resObj;

    }

    @Override
    public GetUserInfoResponse getUserInfo(String username, String prefix) {
        GetUserInfoResponse response = new GetUserInfoResponse();
        Optional<WebUser> optWebUser = webUserRepository.findFirstByUsernameAndAgent_Prefix(username, prefix);
        if (optWebUser.isPresent()) {
            WebUser webUser = optWebUser.get();
            response.setName(webUser.getFirstName() + " " + webUser.getLastName());
            response.setTel(webUser.getTel());
            response.setBankName(webUser.getBankName());
            response.setBankAccount(webUser.getAccountNumber());

            Optional<Wallet> optWallet = walletRepository.findByUser_Id(webUser.getId());
            if (optWallet.isPresent()) {
                Wallet wallet = optWallet.get();
                response.setTurnOver(wallet.getTurnOver());
                response.setCredit(wallet.getCredit());
            }
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01127);
        }

        return response;
    }


}
