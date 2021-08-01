package com.game.b1ingservice.postgres.jdbc;

import com.game.b1ingservice.payload.admin.CountRefillRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.jdbc.dto.CountRefillDTO;
import com.game.b1ingservice.service.AdminService;
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
    @Autowired
    AdminService adminService;

    public List<CountRefillDTO> depositCount(CountRefillRequest countRefillRequest, UserPrincipal principal) {
        List<CountRefillDTO> deposit = new ArrayList<>();
        try {
            String username = countRefillRequest.getUsername();
            if (username != null && !"".equals(username)) username = username.toLowerCase();

            StringBuilder sql = new StringBuilder();
            sql.append("select us.id, us.username, CASE WHEN dh.count IS NULL THEN 0 ELSE dh.count END as  depositCount, CASE WHEN dh.deposit IS NULL THEN 0 ELSE dh.deposit END as deposit, CASE WHEN wh.count IS NULL THEN 0 ELSE wh.count END as withdrawCount, CASE WHEN wh.withdraw IS NULL THEN 0 ELSE wh.withdraw END as withdraw , (CASE WHEN dh.deposit IS NULL THEN 0 ELSE dh.deposit END - CASE WHEN wh.withdraw IS NULL THEN 0 ELSE wh.withdraw END )as profitloss ");
            sql.append("from users us ");
            sql.append("left join (select (u.username) as username, count(d.id) as count, sum(d.amount) as deposit ");
            sql.append("from users u ");
            sql.append("inner join agent a on u.agent_id = a.id ");
            sql.append("left join deposit_history d on u.id = d.user_id ");
            sql.append("where d.created_date between TO_TIMESTAMP( ? , 'YYYY-MM-DD HH24:MI:SS') ");
            sql.append("and TO_TIMESTAMP( ? , 'YYYY-MM-DD HH24:MI:SS') ");
            sql.append("and a.id = ? ");
            sql.append("and d.status = 'SUCCESS' and (d.mistake_type is null or d.mistake_type = 'NO_SLIP') ");
            sql.append("group by u.username) dh on dh.username = us.username ");
            sql.append("left join (select (u.username) as username, count(wh.id) as count, sum(wh.amount) as withdraw  ");
            sql.append("from users u ");
            sql.append("inner join agent a on u.agent_id = a.id ");
            sql.append("left join withdraw_history wh on u.id = wh.user_id ");
            sql.append("where wh.created_date between TO_TIMESTAMP( ? , 'YYYY-MM-DD HH24:MI:SS') ");
            sql.append("and TO_TIMESTAMP( ? , 'YYYY-MM-DD HH24:MI:SS') ");
            sql.append("and a.id = ? ");
            sql.append("and wh.status = 'SUCCESS' and wh.mistake_type is null ");
            sql.append("group by u.username) wh on wh.username = us.username ");
            sql.append("where (dh.count is not null) ");
            if (username != null && !"".equals(username)) {
                sql.append("and us.username = ? ");
            }

            if (username != null && !"".equals(username)) {
                deposit = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(CountRefillDTO.class),
                        countRefillRequest.getListDateFrom(), countRefillRequest.getListDateTo(), principal.getAgentId(),
                        countRefillRequest.getListDateFrom(), countRefillRequest.getListDateTo(), principal.getAgentId(),
                        username);
            } else {
                deposit = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(CountRefillDTO.class),
                        countRefillRequest.getListDateFrom(), countRefillRequest.getListDateTo(), principal.getAgentId(),
                        countRefillRequest.getListDateFrom(), countRefillRequest.getListDateTo(), principal.getAgentId());
            }

        } catch (Exception e) {
            log.error("countRefill", e);
        }
        return deposit;
    }
}

