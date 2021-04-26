package com.game.b1ingservice.postgres.jdbc;

import com.game.b1ingservice.payload.affiliate.AffHistoryRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.jdbc.dto.SearchAffiHistoryDTO;
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
public class SearchAffiliateHistoryJdbcRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public List<SearchAffiHistoryDTO> affiHistory(AffHistoryRequest affHistoryRequest, UserPrincipal principal) {
        List<SearchAffiHistoryDTO> search = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select  u2.username as username ,sum(ph.amount) as amount ");
            sql.append("from point_history ph ");
            sql.append("inner join users u2 on ph.user_id = u2.id ");
            sql.append("inner join agent a on u2.agent_id = a.id ");
            sql.append("where ph.created_date between TO_TIMESTAMP( ? ,'DD/MM/YYYY HH24:MI:SS') ");
            sql.append("and TO_TIMESTAMP( ? ,'DD/MM/YYYY HH24:MI:SS') ");
            if ((null != affHistoryRequest.getUsername()) && (!"".equals(affHistoryRequest.getUsername()))) {
                sql.append("and u2.username = ? ");
            }

            sql.append("and ph.status ='SUCCESS' ");
            sql.append("and ph.type = 'EARN_POINT' ");
            sql.append("and a.prefix = ? ");
            sql.append("group by u2.username ");
            if ((null != affHistoryRequest.getUsername()) && (!"".equals(affHistoryRequest.getUsername()))) {
                search = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(SearchAffiHistoryDTO.class), affHistoryRequest.getListDateFrom(), affHistoryRequest.getListDateTo(), affHistoryRequest.getUsername(), principal.getPrefix());
            } else {
                search = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(SearchAffiHistoryDTO.class), affHistoryRequest.getListDateFrom(), affHistoryRequest.getListDateTo(), principal.getPrefix());
            }

        } catch (Exception e) {
            log.error("SearchAffiliateHistory", e);
        }
        return search;
    }
}
