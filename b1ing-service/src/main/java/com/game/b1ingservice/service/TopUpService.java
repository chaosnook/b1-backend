package com.game.b1ingservice.service;

import com.game.b1ingservice.payload.admin.ProfitReportRequest;
import com.game.b1ingservice.payload.admin.ProfitReportResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.userinfo.UserProfile;
import com.game.b1ingservice.payload.webuser.WebUserHistoryRequest;
import com.game.b1ingservice.payload.webuser.WebUserHistoryResponse;
import org.springframework.stereotype.Service;

@Service
public interface TopUpService {
    ProfitReportResponse topupReport(ProfitReportRequest profitReportRequest, UserPrincipal principal);
}
