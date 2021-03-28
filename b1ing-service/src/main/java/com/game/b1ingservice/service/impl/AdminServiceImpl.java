package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.admin.*;
import com.game.b1ingservice.payload.amb.*;
import com.game.b1ingservice.payload.bankbot.BankBotScbWithdrawCreditRequest;
import com.game.b1ingservice.payload.bankbot.BankBotScbWithdrawCreditResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.entity.*;
import com.game.b1ingservice.postgres.jdbc.CountRefillJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.ProfitLossJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.ProfitReportJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.dto.*;
import com.game.b1ingservice.postgres.repository.*;
import com.game.b1ingservice.service.*;
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
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.game.b1ingservice.commons.Constants.MESSAGE_ADMIN_DEPOSIT;
import static com.game.b1ingservice.commons.Constants.MESSAGE_DEPOSIT;

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
    WithdrawHistoryRepository withdrawHistoryRepository;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    ProfitReportJdbcRepository profitReportJdbcRepository;
    @Autowired
    private AMBService ambService;
    @Autowired
    private BankBotService bankBotService;
    @Autowired
    private AffiliateService affiliateService;
    @Autowired
    ProfitLossJdbcRepository profitLossJdbcRepository;
    @Autowired
    CountRefillJdbcRepository countRefillJdbcRepository;

    @Autowired
    private LineNotifyService lineNotifyService;

    @Autowired
    RoleRepository rolerepository;

    @Override
    public ResponseEntity<?> loginAdmin(String username, String password, LoginRequest loginRequest) {
        Optional<AdminUser> opt = adminUserRepository.findByUsernameAndPrefixAndActive(username, loginRequest.getPrefix(), 1);
        if (opt.isPresent()) {
            AdminUser admin = opt.get();
            if (bCryptPasswordEncoder.matches(password, admin.getPassword())) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", admin.getId());
                claims.put("username", admin.getUsername());
                if (ObjectUtils.isNotEmpty(admin.getAgent()))
                    claims.put("agentId", admin.getAgent().getId());
                claims.put("type", admin.getRole().getRoleCode());
                claims.put("prefix", admin.getPrefix());

                LoginProfile profile = new LoginProfile(admin.getRole().getRoleCode(), admin.getUsername(), admin.getFullName(), admin.getPrefix());
                return ResponseEntity.ok(new LoginResponse(jwtTokenUtil.generateToken(claims, "user"), profile));
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
        if (opt.isPresent()) {
            adminUser.setRole(opt.get());
        } else {
            opt = rolerepository.findByRoleCode("STAFF");
            adminUser.setRole(opt.get());
        }
        adminUserRepository.save(adminUser);
    }

    @Override
    public void updateAdmin(AdminUpdateRequest adminUpdateRequest, UserPrincipal principal) {
        Optional<AdminUser> opt = adminUserRepository.findById(adminUpdateRequest.getId());
        if (opt.isPresent()) {
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
            if (optRole.isPresent()) {
                adminUser.setRole(optRole.get());
            } else {
                optRole = rolerepository.findByRoleCode("STAFF");
                adminUser.setRole(optRole.get());
            }
            adminUserRepository.save(adminUser);
        } else {
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
        if (opt.isPresent()) {
            WebUser webUser = opt.get();
            Optional<Wallet> opt2 = walletRepository.findByUser_Id(webUser.getId());
            if (opt2.isPresent()) {
                Wallet wallet = opt2.get();
                BigDecimal beforAmount = wallet.getCredit();
                BigDecimal credit = req.getCredit().setScale(2, RoundingMode.HALF_DOWN);
                Bank bank = wallet.getBank();
                BigDecimal afterAmount = beforAmount.add(credit);

                DepositHistory depositHistory = new DepositHistory();
                depositHistory.setAmount(credit);
                depositHistory.setBeforeAmount(beforAmount);
                depositHistory.setAfterAmount(afterAmount);
                depositHistory.setUser(webUser);
                depositHistory.setBank(bank);
                depositHistory.setStatus(Constants.DEPOSIT_STATUS.PENDING);

                Optional<AdminUser> adminOpt = adminUserRepository.findById(principal.getId());

                if (adminOpt.isPresent()) {
                    depositHistory.setAdmin(adminOpt.get());
                }

                depositHistoryRepository.save(depositHistory);

                Agent agent = wallet.getUser().getAgent();
                AmbResponse<DepositRes> ambResponse = ambService.deposit(DepositReq.builder()
                    .amount(credit.toPlainString())
                    .build(), req.getUsername(), agent);

                String errorMessage = "";
                if (ambResponse.getCode() == 0) {

                    lineNotifyService.sendLineNotifyMessages(String.format(MESSAGE_ADMIN_DEPOSIT,principal.getUsername(), req.getUsername() , req.getCredit()) ,
                            agent.getLineToken());

                    walletRepository.depositCredit(credit, webUser.getId());
                    webUserRepository.updateDepositRef(ambResponse.getResult().getRef(), webUser.getId());
                    // check affiliate
                    affiliateService.earnPoint(webUser.getId(), credit, principal.getPrefix());
                    depositHistory.setStatus(Constants.DEPOSIT_STATUS.SUCCESS);
                } else {
                    //if error
                    depositHistory.setReason(errorMessage);
                    depositHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                }

                depositHistoryRepository.save(depositHistory);

            } else {
                throw new ErrorMessageException(Constants.ERROR.ERR_01132);
            }
        } else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01127);
        }

    }

    @Override
    public void withdrawCredit(WithdrawRequest req, UserPrincipal principal) {
        Optional<WebUser> opt = webUserRepository.findFirstByUsernameAndAgent_Prefix(req.getUsername(), principal.getPrefix());
        if (opt.isPresent()) {
            WebUser webUser = opt.get();

                Wallet wallet = webUser.getWallet();

                if (wallet.getCredit().compareTo(req.getCredit()) < 0) {
                    throw new ErrorMessageException(Constants.ERROR.ERR_01133);
                }

                BigDecimal beforAmount = wallet.getCredit();
                Bank bank = wallet.getBank();
                BigDecimal afterAmount = beforAmount.subtract(req.getCredit());

                WithdrawHistory withdrawHistory = new WithdrawHistory();
                withdrawHistory.setAmount(req.getCredit());
                withdrawHistory.setBeforeAmount(beforAmount);
                withdrawHistory.setAfterAmount(afterAmount);
                withdrawHistory.setUser(webUser);
                withdrawHistory.setBank(bank);
                withdrawHistory.setStatus(Constants.DEPOSIT_STATUS.PENDING);

                Optional<AdminUser> adminOpt = adminUserRepository.findById(principal.getId());
                if (adminOpt.isPresent()) {
                    withdrawHistory.setAdmin(adminOpt.get());
                }

                withdrawHistoryRepository.save(withdrawHistory);

                AmbResponse<WithdrawRes> ambResponse = ambService.withdraw(WithdrawReq.builder()
                        .amount(req.getCredit().toString())
                        .build(), req.getUsername(), wallet.getUser().getAgent());

                String errorMessage = "";
                if (ambResponse.getCode() == 0) {
                    walletRepository.withDrawCredit(req.getCredit(), webUser.getId());

                    BankBotScbWithdrawCreditRequest request = new BankBotScbWithdrawCreditRequest();
                    request.setAmount(req.getCredit());
                    request.setAccountTo(webUser.getAccountNumber());
                    request.setBankCode(webUser.getBankName());

                    BankBotScbWithdrawCreditResponse depositResult = bankBotService.withDrawCredit(request);
                    if (depositResult.getStatus()){
                        withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.SUCCESS);
                        withdrawHistory.setReason(depositResult.getQrString());
                    }else {
                        withdrawHistory.setStatus(Constants.WITHDRAW_STATUS.ERROR);
                        withdrawHistory.setReason(depositResult.getMessege());
                    }
                    withdrawHistoryRepository.save(withdrawHistory);

                } else {
                    //if error
                    withdrawHistory.setReason(errorMessage);
                    withdrawHistory.setStatus(Constants.DEPOSIT_STATUS.ERROR);
                    withdrawHistoryRepository.save(withdrawHistory);

                    throw new ErrorMessageException(Constants.ERROR.ERR_10001);
                }

            }
        else {
            throw new ErrorMessageException(Constants.ERROR.ERR_01127);
        }
    }

    @Override
    public ProfitReportResponse profitReport(ProfitReportRequest profitReportRequest, UserPrincipal principal) {
        Optional<Agent> agent = agentRepository.findByPrefix(principal.getPrefix());


        if (!agent.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        ProfitReportResponse resObj = new ProfitReportResponse();
        String value = profitReportRequest.getValue();

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
            resObj.getData().add(BigDecimal.valueOf(0));

        }
        List<ProfitReport> listDepositMonth = profitReportJdbcRepository.depositReport(profitReportRequest, principal);
        List<ProfitReport> listWithdrawMonth = profitReportJdbcRepository.withdrawReport(profitReportRequest, principal);


        for (int i = 0; i< resObj.getLabels().size(); i++) {
            int finalI = i;
            Optional<ProfitReport> depO = listDepositMonth.stream().filter(dep -> dep.getLabels() == finalI +1).findFirst();

            BigDecimal depValue = new BigDecimal(0);
            if (depO.isPresent()) {
                depValue = depO.get().getData();
            }

            Optional<ProfitReport> withO = listWithdrawMonth.stream().filter(with -> with.getLabels() == finalI +1).findFirst();

            BigDecimal withValue = new BigDecimal(0);
            if (withO.isPresent()) {
                withValue = withO.get().getData();
            }

                resObj.getData().set(i, depValue.subtract(withValue));

        }

        return resObj;
    }

    @Override
    public ProfitLossResponse profitLoss(ProfitLossRequest profitLossRequest, UserPrincipal principal) {
        Optional<Agent> agent = agentRepository.findByPrefix(principal.getPrefix());


        if (!agent.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        ProfitLossResponse resObj = new ProfitLossResponse();

        List<ProfitLoss> listDeposit = profitLossJdbcRepository.depositProfit(profitLossRequest, principal);
        List<ProfitLoss> listWithdraw = profitLossJdbcRepository.withdrawProfit(profitLossRequest, principal);

        for(ProfitLoss profitLoss: listDeposit) {
            resObj.setDeposit(profitLoss.getDeposit());
            resObj.setBonus(profitLoss.getBonus());
            resObj.setDepositBonus(profitLoss.getDepositBonus());
        }
        for(ProfitLoss profitLoss: listWithdraw) {
            resObj.setWithdraw(profitLoss.getWithdraw());
            resObj.setProfitLoss(resObj.getDepositBonus().subtract(profitLoss.getWithdraw()));
        }

        return resObj;
    }

    @Override
    public List<CountRefillDTO> countRefill(CountRefillRequest countRefillRequest, UserPrincipal principal) {
        Optional<Agent> agent = agentRepository.findByPrefix(principal.getPrefix());

        if (!agent.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        CountRefillResponse resObj = new CountRefillResponse();

        List<CountRefillDTO> listCountDeposit = countRefillJdbcRepository.depositCount(countRefillRequest, principal);


        List<CountRefillDTO> result = new ArrayList<>();
        for (CountRefillDTO countRefillDTOD : listCountDeposit) {
            CountRefillDTO deposit = new CountRefillDTO();
            deposit.setUsername(countRefillDTOD.getUsername());
            deposit.setDepositCount(countRefillDTOD.getDepositCount());
            deposit.setDeposit(countRefillDTOD.getDeposit());
            deposit.setWithdrawCount(countRefillDTOD.getWithdrawCount());
            deposit.setWithdraw(countRefillDTOD.getWithdraw());
            deposit.setProfitLoss(countRefillDTOD.getProfitLoss());
            result.add(deposit);
        }

        return result;
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

    @Override
    public void withdrawManual(WithdrawManualReq req, UserPrincipal principal) {

        String reason = "Withdraw Manual";
        withdrawHistoryRepository.updateInfoWithdrawManual(Constants.WITHDRAW_STATUS.SUCCESS, reason, req.getId(), req.getAmount());

    }

    @Override
    public void approve(ApproveReq req, UserPrincipal principal) {

        withdrawHistoryRepository.updateStatus(Constants.WITHDRAW_STATUS.SUCCESS, req.getId(), req.getAmount());

    }
}
