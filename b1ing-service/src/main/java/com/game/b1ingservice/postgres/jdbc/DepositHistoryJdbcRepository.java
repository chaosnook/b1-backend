package com.game.b1ingservice.postgres.jdbc;

import com.game.b1ingservice.postgres.jdbc.dto.DepositHistoryTop20Dto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class DepositHistoryJdbcRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public List<DepositHistoryTop20Dto> findTop20DepositHistory(String username, Long agentId) {

        List<DepositHistoryTop20Dto> result = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select d.amount as amount, d.bonus_amount as bonus, d.before_amount as beforeAmount, (d.amount + d.bonus_amount) as addCredit, ")
                      .append("d.after_amount as afterAmount, d.created_date as createdDate, d.reason as reason ")
                    .append("from deposit_history d ")
                    .append("inner join users u on d.user_id = u.id ")
                    .append("inner join agent a on u.agent_id = a.id ")
                    .append("where u.username = ? and a.id = ? ")
                    .append("order by d.created_date desc limit 20");

            result = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(DepositHistoryTop20Dto.class), username, agentId);

        } catch (Exception ex) {
            log.error("DepositHistoryTop20", ex);
        }

        return result;

    }
}
