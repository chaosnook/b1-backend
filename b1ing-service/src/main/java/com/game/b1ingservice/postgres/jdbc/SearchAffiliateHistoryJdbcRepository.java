package com.game.b1ingservice.postgres.jdbc;

import com.game.b1ingservice.payload.admin.CountRefillRequest;
import com.game.b1ingservice.payload.affiliate.AffHistoryRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.postgres.jdbc.dto.CountRefillDTO;
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
            String sql = "select  u2.username as username ,sum(ph.amount) as amount " +
                    "from point_history ph " +
                    "inner join users u2 on ph.user_id = u2.id " +
                    "where ph.created_date between TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') " +
                    "and TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') " +
                    "and u2.username = ? " +
                    "and ph.type = 'EARN_POINT' " +
                    "group by u2.username ";

            search = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SearchAffiHistoryDTO.class), affHistoryRequest.getListDateFrom(),affHistoryRequest.getListDateTo(),affHistoryRequest.getUsername());
        } catch (Exception e) {
            log.error("SearchAffiliateHistory", e);
        }
        return search;
    }
}
