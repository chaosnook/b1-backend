package com.game.b1ingservice.postgres.jdbc;

import com.game.b1ingservice.payload.admin.ProfitReportRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.topup.TopupRequest;
import com.game.b1ingservice.postgres.jdbc.dto.ProfitReport;
import com.game.b1ingservice.postgres.jdbc.dto.TopupReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class TopupReportJdbcRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public List<TopupReport> topupReportList(TopupRequest topupRequest, UserPrincipal principal) {
        List<TopupReport> topup = new ArrayList<>();
        try {
            String sql = "select " +
                    "     bank.bank_name as labels , " +
                    "     sum(deposit_history.amount) as data " +
                    "     " +
                    "from deposit_history " +
                    "inner join bank on deposit_history.bank_code = bank.id " +
                    "inner join users u on deposit_history.user_id = u.id " +
                    "inner join agent a on u.agent_id = a.id  " +
                    "where to_char(deposit_history.created_date, 'YYYY-MM-DD') between ? and ? " +
                    "and a.prefix = ? " +
                    "group by bank.bank_name  " ;

            topup = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TopupReport.class), topupRequest.getListDateFrom(),topupRequest.getListDateTo(), principal.getPrefix());

        } catch (Exception e) {
            log.error("topupReport", e);
        }
        return topup;
    }
}
