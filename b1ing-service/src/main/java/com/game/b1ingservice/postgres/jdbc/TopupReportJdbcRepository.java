package com.game.b1ingservice.postgres.jdbc;

import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.topup.TopupRequest;
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
                    "from deposit_history " +
                    "inner join bank on deposit_history.bank_code = bank.id " +
                    "where deposit_history.created_date between TO_TIMESTAMP(?, 'yyyy-MM-DD HH24:MI:SS') and TO_TIMESTAMP(?, 'yyyy-MM-DD HH24:MI:SS') " +
                    "and deposit_history.agent_id = ? and deposit_history.status = 'SUCCESS' " +
                    "group by bank.bank_name ";

            topup = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TopupReport.class),
                    topupRequest.getListDateFrom().concat(" 00:00:00"),
                    topupRequest.getListDateTo().concat(" 23:59:59"),
                    principal.getAgentId());

        } catch (Exception e) {
            log.error("topupReport", e);
        }
        return topup;
    }
}
