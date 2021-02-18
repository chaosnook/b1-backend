package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.admin.ProfitReportRequest;
import com.game.b1ingservice.payload.admin.ProfitReportResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.jdbc.ProfitReportJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.dto.ProfitReport;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.service.AdminService;
import com.game.b1ingservice.service.TopUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class TopUpServiceImpl implements TopUpService {

    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    ProfitReportJdbcRepository profitReportJdbcRepository;
    @Override
    public ProfitReportResponse topupReport(ProfitReportRequest profitReportRequest, UserPrincipal principal) {
        Optional<Agent> agent = agentRepository.findByPrefix(principal.getPrefix());

        if (!agent.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        ProfitReportResponse resObj = new ProfitReportResponse();
        String value = profitReportRequest.getValue();

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
            resObj.getData().add(BigDecimal.valueOf(0));
        }

        List<ProfitReport> list = profitReportJdbcRepository.depositReport(profitReportRequest, principal);

        for (ProfitReport profitReport : list) {
            resObj.getData().set(profitReport.getLabels() - 1, profitReport.getData());
        }


        return resObj;
    }

}
