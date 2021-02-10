package com.game.b1ingservice.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.admin.*;
import com.game.b1ingservice.payload.agent.AgentResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.entity.*;
import com.game.b1ingservice.postgres.repository.*;
import com.game.b1ingservice.service.AdminService;
import com.game.b1ingservice.utils.JwtTokenUtil;
import com.game.b1ingservice.utils.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminUserRepository adminUserRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    WebUserRepository webUserRepository;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    DepositHistoryRepository depositHistoryRepository;


    @Autowired
    RoleRepository rolerepository;

    @Override
    public ResponseEntity<?> loginAdmin(String username, String password, LoginRequest loginRequest) {
        Optional<AdminUser> opt = adminUserRepository.findByUsernameAndPrefixAndActive(username,loginRequest.getPrefix(),1);
        if (opt.isPresent()){
            AdminUser admin = opt.get();
            if (bCryptPasswordEncoder.matches(password,admin.getPassword())){
                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", admin.getId());
                claims.put("username", admin.getUsername());
                if (ObjectUtils.isNotEmpty(admin.getAgent()))
                    claims.put("agentId", admin.getAgent().getId());
                claims.put("type", admin.getRole().getRoleCode());
                claims.put("prefix", admin.getPrefix());

                LoginProfile profile = new LoginProfile(admin.getRole().getRoleCode(), admin.getUsername(), admin.getFullName(), admin.getPrefix());
                return ResponseEntity.ok(new LoginResponse(jwtTokenUtil.generateToken(claims, "user"),profile));
            }
        }
        return ResponseHelper.bad(Constants.ERROR.ERR_00007.msg);

    }

    @Override
    public void registerAdmin(RegisterRequest registerRequest, UserPrincipal principal) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(registerRequest.getUsername());
        adminUser.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        adminUser.setTel(registerRequest.getTel());
        adminUser.setFullName(registerRequest.getFullName());
        adminUser.setLimit(registerRequest.getLimit());
        adminUser.setLimitFlag(registerRequest.getIsLimit());
        adminUser.setActive(registerRequest.getActive());
        adminUser.setPrefix(principal.getPrefix());

        Optional<Role> opt = rolerepository.findByRoleCode(registerRequest.getRoleCode());
        if (opt.isPresent()){
            adminUser.setRole(opt.get());
        }else {
            opt = rolerepository.findByRoleCode("STAFF");
            adminUser.setRole(opt.get());
        }
        adminUserRepository.save(adminUser);
    }

    @Override
    public void updateAdmin(AdminUpdateRequest adminUpdateRequest,UserPrincipal principal) {
        Optional<AdminUser> opt = adminUserRepository.findById(adminUpdateRequest.getId());
        if (opt.isPresent()){
            AdminUser adminUser = opt.get();

            if (!StringUtils.isEmpty(adminUpdateRequest.getPassword())) {
                adminUser.setPassword(bCryptPasswordEncoder.encode(adminUpdateRequest.getPassword()));
            }

            adminUser.setTel(adminUpdateRequest.getTel());
            adminUser.setFullName(adminUpdateRequest.getFullName());
            adminUser.setLimit(adminUpdateRequest.getLimit());
            adminUser.setLimitFlag(adminUpdateRequest.getIsLimit());
            adminUser.setActive(adminUpdateRequest.getActive());
            adminUser.setPrefix(principal.getPrefix());

            Optional<Role> optRole = rolerepository.findByRoleCode(adminUpdateRequest.getRoleCode());
            if (optRole.isPresent()){
                adminUser.setRole(optRole.get());
            }else {
                optRole = rolerepository.findByRoleCode("STAFF");
                adminUser.setRole(optRole.get());
            }
            adminUserRepository.save(adminUser);
        }else {
            throw new ErrorMessageException(Constants.ERROR.ERR_00009);
        }
    }

    @Override
    public List<AdminUserResponse> listByPrefix(String prefix) {
         List<AdminUserResponse> list = adminUserRepository.findByPrefix(prefix).stream().map(converter).collect(Collectors.toList());
         return list;
    }

    @Override
    public AdminUserResponse findAdminByUsernamePrefix(String username, String prefix) {
        Optional<AdminUser> opt = adminUserRepository.findByUsernameAndPrefixAndActive(username, prefix, 1);
        if (opt.isPresent()) {
            return converter.apply(opt.get());
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_00009);
        }
    }

    @Override
    public void addCredit(AddCreditRequest req, UserPrincipal principal) {
        Optional<WebUser> opt = webUserRepository.findFirstByUsernameAndAgent_Prefix(req.getUsername(), principal.getPrefix());
        if(opt.isPresent()) {
            WebUser webUser = opt.get();
            Optional<Wallet> opt2 = walletRepository.findByUser_Id(webUser.getId());
            if(opt2.isPresent()) {
                Wallet wallet =  opt2.get();
                BigDecimal beforAmount = wallet.getCredit();
                Bank bank = wallet.getBank();
                BigDecimal afterAmount = beforAmount.add(req.getCredit());
                wallet.setCredit(afterAmount);
                walletRepository.save(wallet);

                DepositHistory depositHistory = new DepositHistory();
                depositHistory.setAmount(req.getCredit());
                depositHistory.setBeforeAmount(beforAmount);
                depositHistory.setAfterAmount(afterAmount);
                depositHistory.setUser(webUser);
                depositHistory.setBank(bank);

                Optional<AdminUser> adminOpt = adminUserRepository.findById(principal.getId());
                if(adminOpt.isPresent()) {
                    depositHistory.setAdmin(adminOpt.get());
                }

                depositHistoryRepository.save(depositHistory);

            } else {
                throw new ErrorMessageException(Constants.ERROR.ERR_01132);
            }
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01127);
        }

    }


    Function<AdminUser, AdminUserResponse> converter = admin -> {
        AdminUserResponse response = new AdminUserResponse();
        response.setId(admin.getId());
        response.setCreatedDate(admin.getCreatedDate());
        response.setUpdatedDate(admin.getUpdatedDate());
        response.setCreatedBy(admin.getAudit().getCreatedBy());
        response.setUpdatedBy(admin.getAudit().getUpdatedBy());
        response.setDeleteFlag(admin.getDeleteFlag());
        response.setVersion(admin.getVersion());

        response.setUsername(admin.getUsername());
        response.setTel(admin.getTel());
        response.setFullName(admin.getFullName());
        response.setLimitFlag(admin.getLimitFlag());
        response.setLimit(admin.getLimit());
        response.setActive(admin.getActive());
        response.setRole(admin.getRole().getRoleCode());
        return response;
    };
}
