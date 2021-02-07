package com.game.b1ingservice.postgres.jdbc;

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

    public List<SummaryRegisterUser> summaryRegisterUsersByDay(String day) {
        List<SummaryRegisterUser> result = new ArrayList<>();
        try {
            String sql = "select   " +
                    "    extract(hour from created_date) as hourOfDay ,  " +
                    "    count(created_by), " +
                    "    agent_id as agentId " +
                    "from users  " +
                    "    where created_date::date = ? " +
                    "    and agent_id = 1 " +
                    "group by extract(hour from created_date),agent_id  " +
                    "order by hourOfDay asc";
            result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SummaryRegisterUser.class), day);
        } catch (Exception e) {
            log.error("summaryRegisterUsers", e);
        }
        return result;
    }

    public List<SummaryRegisterUser> summaryRegisterUsersByMonth(String month){
        List<SummaryRegisterUser> result = new ArrayList<>();
        try{
            String sql = "select   " +
                    "    extract(hour from created_date) as hourOfDay ,  " +
                    "    count(created_by), " +
                    "    agent_id as agentId " +
                    "from users  " +
                    "    where created_date::date = '2021-02-01' " +
                    "    and agent_id = 1 " +
                    "group by extract(hour from created_date),agent_id  " +
                    "order by hourOfDay asc";
            result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SummaryRegisterUser.class), month);
        }catch (Exception e) {
            log.error("summaryRegisterUsers", e);
        }
        return result;
    }

    public List<SummaryRegisterUser> summaryRegisterUsersByYear(String year){
        List<SummaryRegisterUser> result = new ArrayList<>();
        try{
            String sql = "select   " +
                    "    extract(hour from created_date) as hourOfDay ,  " +
                    "    count(created_by), " +
                    "    agent_id as agentId " +
                    "from users  " +
                    "    where created_date::date = '2021-02-01' " +
                    "    and agent_id = 1 " +
                    "group by extract(hour from created_date),agent_id  " +
                    "order by hourOfDay asc";
            result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SummaryRegisterUser.class), year);
        }catch (Exception e) {
            log.error("summaryRegisterUsers", e);
        }
        return result;
    }

}
