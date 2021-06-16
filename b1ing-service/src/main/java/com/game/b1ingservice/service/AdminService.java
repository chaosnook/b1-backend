package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.admin.*;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.jdbc.dto.CountRefillDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    ResponseEntity<?> loginAdmin(String username, String password, LoginRequest loginRequest);

    void registerAdmin(RegisterRequest registerRequest, UserPrincipal principal);

    void updateAdmin(AdminUpdateRequest adminUpdateRequest, UserPrincipal principal);

    List<AdminUserResponse> listByPrefix(String prefix);

    AdminUserResponse findAdminByUsernameAndAgentId(String username, Long agentId);

    void addCredit(AddCreditRequest req, UserPrincipal principal);

    void withdrawCredit(WithdrawRequest req, UserPrincipal principal);

    void withdrawManual(WithdrawManualReq req, UserPrincipal principal);

    void approve(ApproveReq req, UserPrincipal principal);

    ProfitReportResponse profitReport(ProfitReportRequest profitReportRequest, UserPrincipal principal);

    ProfitLossResponse profitLoss(ProfitLossRequest profitLossRequest, UserPrincipal principal);

    List<CountRefillDTO> countRefill(CountRefillRequest countRefillRequest, UserPrincipal principal);

    boolean checkAdmin(Long userId, String prefix);

    boolean checkLastUQToken(Long id, String lastUUID);
}
