package com.game.b1ingservice.postgres.jdbc;

import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.webuser.WebUserHistoryRequest;
import com.game.b1ingservice.postgres.jdbc.dto.SummaryRegisterUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class WebUserJdbcRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public List<SummaryRegisterUser> summaryRegisterUsersByDay(WebUserHistoryRequest webUserHistoryRequest, UserPrincipal principal) {
        List<SummaryRegisterUser> result = new ArrayList<>();
        try {
            String sql = "select   " +
                    "    extract(hour from created_date) as labels ,  " +
                    "    count(created_by) as data, " +
                    "    agent_id as agentId " +
                    "from users  " +
                    "    where created_date::date = ? " +
                    "    and agent_id = ? " +
                    "group by extract(hour from created_date),agent_id  " +
                    "order by labels asc";
            result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SummaryRegisterUser.class), webUserHistoryRequest.getValue(), principal.getAgentId());
        } catch (Exception e) {
            log.error("summaryRegisterUsers", e);
        }
        return result;
    }

    public List<SummaryRegisterUser> summaryRegisterUsersByMonth(WebUserHistoryRequest webUserHistoryRequest, UserPrincipal principal){
        List<SummaryRegisterUser> result = new ArrayList<>();
        try{
            String sql = "select " +
                    "    extract(day from created_date) as labels , " +
                    "    count(created_by) as data, " +
                    "    agent_id as agentId " +
                    "from users " +
                    "where to_char(created_date, 'YYYY-MM') = ? " +
                    "and agent_id = ? " +
                    "group by extract(day from created_date), agent_id  " +
                    "order by labels asc";
            result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SummaryRegisterUser.class), webUserHistoryRequest.getValue(), principal.getAgentId());
        }catch (Exception e) {
            log.error("summaryRegisterUsers", e);
        }
        return result;
    }

    public List<SummaryRegisterUser> summaryRegisterUsersByYear(WebUserHistoryRequest webUserHistoryRequest, UserPrincipal principal){
        List<SummaryRegisterUser> result = new ArrayList<>();
        try{
            String sql = "select " +
                    "    extract(month from created_date) as labels , " +
                    "    count(created_by) as data, " +
                    "    agent_id as agentId " +
                    "from users " +
                    "where to_char(created_date, 'YYYY') = ? " +
                    "and agent_id = ? " +
                    "group by extract(month from created_date), agent_id  " +
                    "order by labels asc";
            result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SummaryRegisterUser.class), webUserHistoryRequest.getValue(), principal.getAgentId());
        }catch (Exception e) {
            log.error("summaryRegisterUsers", e);
        }
        return result;
    }

}
