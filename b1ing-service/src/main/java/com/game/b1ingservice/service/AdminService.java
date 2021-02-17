package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.admin.*;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.webuser.WebUserHistoryRequest;
import com.game.b1ingservice.payload.webuser.WebUserHistoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    ResponseEntity<?> loginAdmin(String username, String password, LoginRequest loginRequest);
    void registerAdmin(RegisterRequest registerRequest, UserPrincipal principal);
    void updateAdmin(AdminUpdateRequest adminUpdateRequest, UserPrincipal principal);
    List<AdminUserResponse> listByPrefix(String prefix);

    AdminUserResponse findAdminByUsernamePrefix(String username, String prefix);

    void addCredit(AddCreditRequest req, UserPrincipal principal);
    void withdrawCredit(WithdrawRequest req, UserPrincipal principal);

    ProfitReportResponse profitReport(ProfitReportRequest profitReportRequest, UserPrincipal principal);

}
