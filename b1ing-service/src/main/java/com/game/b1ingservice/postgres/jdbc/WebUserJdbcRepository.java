package com.game.b1ingservice.postgres.jdbc;

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

    public List<SummaryRegisterUser> summaryRegisterUsersByDay(WebUserHistoryRequest webUserHistoryRequest) {
        List<SummaryRegisterUser> result = new ArrayList<>();
        try {
            String sql = "select   " +
                    "    extract(hour from created_date) as labels ,  " +
                    "    count(created_by) as data, " +
                    "    agent_id as agentId " +
                    "from users  " +
                    "    where created_date::date = ? " +
                    "    and agent_id = 1 " +
                    "group by extract(hour from created_date),agent_id  " +
                    "order by labels asc";
            result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SummaryRegisterUser.class), webUserHistoryRequest.getValue());
        } catch (Exception e) {
            log.error("summaryRegisterUsers", e);
        }
        return result;
    }

    public List<SummaryRegisterUser> summaryRegisterUsersByMonth(WebUserHistoryRequest webUserHistoryRequest){
        List<SummaryRegisterUser> result = new ArrayList<>();
        try{
            String sql = "select " +
                    "    extract(day from created_date) as dayOfMonth , " +
                    "    count(created_by) , " +
                    "    created_by as createdBy " +
                    "from users " +
                    "where to_char(created_date, 'YYYY-MM') = '2021-01' " +
                    "group by extract(day from created_date),created_by " +
                    "order by dayOfMonth asc ";
            result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SummaryRegisterUser.class), webUserHistoryRequest.getValue());
        }catch (Exception e) {
            log.error("summaryRegisterUsers", e);
        }
        return result;
    }

    public List<SummaryRegisterUser> summaryRegisterUsersByYear(WebUserHistoryRequest webUserHistoryRequest){
        List<SummaryRegisterUser> result = new ArrayList<>();
        try{
            String sql = "select " +
                    "    extract(month from created_date) as monthOfYear , " +
                    "    count(created_by) , " +
                    "    created_by as createdBy " +
                    "from users " +
                    "where to_char(created_date, 'YYYY') = '2021' " +
                    "group by extract(month from created_date),created_by " +
                    "order by monthOfYear asc";
            result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SummaryRegisterUser.class), webUserHistoryRequest.getValue());
        }catch (Exception e) {
            log.error("summaryRegisterUsers", e);
        }
        return result;
    }

}
