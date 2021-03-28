package com.game.b1ingservice.postgres.jdbc;

import com.game.b1ingservice.payload.admin.CountRefillRequest;
import com.game.b1ingservice.payload.admin.ProfitLossRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.jdbc.dto.CountRefillDTO;
import com.game.b1ingservice.postgres.jdbc.dto.ProfitLoss;
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
public class CountRefillJdbcRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public List<CountRefillDTO> depositCount(CountRefillRequest countRefillRequest, UserPrincipal principal) {
        List<CountRefillDTO> deposit = new ArrayList<>();
        try {
            String sql = "select  (u.username) as username,count(d.amount) as countDeposit , sum(d.amount) as allDeposit " +
                    "from deposit_history d " +
                    "inner join users u on d.user_id = u.id " +
                    "where d.created_date between TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') " +
                    "and TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') " +
                    "and u.username = ? " +
                    "group by u.username ";

            deposit = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CountRefillDTO.class), countRefillRequest.getListDateFrom(),countRefillRequest.getListDateTo(),countRefillRequest.getUsername());
        } catch (Exception e) {
            log.error("countRefill", e);
        }
        return deposit;
    }
    public List<CountRefillDTO> withdrawCount(CountRefillRequest countRefillRequest, UserPrincipal principal) {
        List<CountRefillDTO> withdraw = new ArrayList<>();
        try {
            String sql = "select  (u.username) as username,count(wh.amount) as countWithdraw , sum(wh.amount) as allWithdraw " +
                    "from withdraw_history wh " +
                    "inner join users u on wh.user_id = u.id " +
                    "where wh.created_date between TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') " +
                    "and TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') " +
                    "and u.username = ? " +
                    "group by u.username ";

            withdraw = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CountRefillDTO.class), countRefillRequest.getListDateFrom(),countRefillRequest.getListDateTo(),countRefillRequest.getUsername());
        } catch (Exception e) {
            log.error("countRefill", e);
        }
        return withdraw;
    }
}
