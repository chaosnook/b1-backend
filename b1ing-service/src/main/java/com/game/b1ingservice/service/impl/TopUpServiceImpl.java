package com.game.b1ingservice.service.impl;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.exception.ErrorMessageException;
import com.game.b1ingservice.payload.admin.ProfitReportRequest;
import com.game.b1ingservice.payload.admin.ProfitReportResponse;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.topup.TopUpResponse;
import com.game.b1ingservice.payload.topup.TopupRequest;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.jdbc.ProfitReportJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.TopupReportJdbcRepository;
import com.game.b1ingservice.postgres.jdbc.dto.ProfitReport;
import com.game.b1ingservice.postgres.jdbc.dto.SummaryRegisterUser;
import com.game.b1ingservice.postgres.jdbc.dto.TopupReport;
import com.game.b1ingservice.postgres.repository.AgentRepository;
import com.game.b1ingservice.service.AdminService;
import com.game.b1ingservice.service.TopUpService;
import com.game.b1ingservice.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class TopUpServiceImpl implements TopUpService {

    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private TopupReportJdbcRepository topupReportJdbcRepository;

    @Override
    public TopUpResponse topUpReport(TopupRequest topupRequest, UserPrincipal principal) {
        Optional<Agent> agent = agentRepository.findByPrefix(principal.getPrefix());

        if (!agent.isPresent()) {
            throw new ErrorMessageException(Constants.ERROR.ERR_PREFIX);
        }

        TopUpResponse resObj = new TopUpResponse();


        List<TopupReport> lists = topupReportJdbcRepository.topupReportList(topupRequest, principal);
        for (TopupReport report : lists) {

            resObj.getLabels().add(report.getLabels());
            resObj.getData().add(report.getData());

        }
        return resObj;
    }
}
