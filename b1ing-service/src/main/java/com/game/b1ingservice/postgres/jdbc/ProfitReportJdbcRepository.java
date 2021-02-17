package com.game.b1ingservice.postgres.jdbc;

import com.game.b1ingservice.payload.admin.ProfitReportRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.webuser.WebUserHistoryRequest;
import com.game.b1ingservice.postgres.jdbc.dto.ProfitReport;
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
public class ProfitReportJdbcRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public List<ProfitReport> depositReport(ProfitReportRequest profitReportRequest, UserPrincipal principal) {
        List<ProfitReport> deposit = new ArrayList<>();
        try {
            String sql = "select " +
                    "    extract(day from created_date) as labels , " +
                    "    sum(amount) as data " +
                    "from deposit_history  " +
                    "where to_char(created_date, 'YYYY-MM') = ? " +
                    "group by extract(day from created_date) " +
                    "order by labels asc";

            deposit = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProfitReport.class), profitReportRequest.getValue());
        } catch (Exception e) {
            log.error("profitReport", e);
        }
        return deposit;
    }

    public List<ProfitReport> withdrawReport(ProfitReportRequest profitReportRequest, UserPrincipal principal) {
        List<ProfitReport> withdraw = new ArrayList<>();
        try {
            String sql = "select " +
                    "    extract(day from created_date) as labels , " +
                    "    sum(amount) as data " +
                    "from withdraw_history  " +
                    "where to_char(created_date, 'YYYY-MM') = ? " +
                    "group by extract(day from created_date) " +
                    "order by labels asc";

            withdraw = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProfitReport.class), profitReportRequest.getValue());
        } catch (Exception e) {
            log.error("profitReport", e);
        }
        return withdraw;
    }
}
