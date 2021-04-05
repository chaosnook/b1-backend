package com.game.b1ingservice.postgres.jdbc;

import com.game.b1ingservice.payload.affiliate.AffHistoryRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.webuser.WebUserHistoryRequest;
import com.game.b1ingservice.payload.webuser.WebUserSearchRequest;
import com.game.b1ingservice.postgres.jdbc.dto.SearchAffiHistoryDTO;
import com.game.b1ingservice.postgres.jdbc.dto.SearchWebUserDTO;
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

    public List<SearchWebUserDTO> searchWebUser(WebUserSearchRequest request, UserPrincipal principal) {
        List<SearchWebUserDTO> search = new ArrayList<>();
        try {
            if(request.isTypeUser()) {

                StringBuilder sql = new StringBuilder();
                sql.append("select id, username, password, tel, bank_name, account_number, first_name, last_name, line, is_bonus, created_date, updated_date ");
                sql.append("from users u ");
                sql.append("where exists ( ");
                sql.append("select ");
                sql.append("from deposit_history dh ");
                sql.append("where user_id = u.id ");
                sql.append(") ");
                sql.append("and u.created_date between TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') ");
                sql.append("and TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS' ) ");

                if((request.getType() != null) && ("" != request.getType())) {
                    if (request.getType().equals("bankName")) {
                        sql.append("and u.bankName = ? ");
                    }
                    if (request.getType().equals("username")) {
                        sql.append("and u.username = ? ");
                    }
                    if (request.getType().equals("accountNumber")) {
                        sql.append("and u.accountNumber = ? ");
                    }
                    if (request.getType().equals("tel")) {
                        sql.append("and u.tel = ? ");
                    }
                    if (request.getType().equals("firstName")) {
                        sql.append("and u.firstName = ? ");
                    }
                    if (request.getType().equals("lastName")) {
                        sql.append("and u.lastName = ? ");
                    }
                    if (request.getType().equals("fullName")) {
                        sql.append("and u.fullName = ? ");
                    }
                }

                sql.append("order by id desc ");

                if((request.getType() != null) && ("" != request.getType()) ) {
                    search = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(SearchWebUserDTO.class), request.getCreatedDateFrom(), request.getCreatedDateTo(), request.getSearchValue());
                } else {

                    search = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(SearchWebUserDTO.class), request.getCreatedDateFrom(), request.getCreatedDateTo());
                }

            } else {

                StringBuilder sql = new StringBuilder();
                sql.append("select id, username, password, tel, bank_name, account_number, first_name, last_name, line, is_bonus, created_date, updated_date ");
                sql.append("from users u ");
                sql.append("where not exists ( ");
                sql.append("select ");
                sql.append("from deposit_history dh ");
                sql.append("where user_id = u.id ");
                sql.append(") ");
                sql.append("and u.created_date between TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') ");
                sql.append("and TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS' ) ");

                if((request.getType() != null) && ("" != request.getType())) {
                    if (request.getType().equals("bankName")) {
                        sql.append("and u.bankName = ? ");
                    }
                    if (request.getType().equals("username")) {
                        sql.append("and u.username = ? ");
                    }
                    if (request.getType().equals("accountNumber")) {
                        sql.append("and u.accountNumber = ? ");
                    }
                    if (request.getType().equals("tel")) {
                        sql.append("and u.tel = ? ");
                    }
                    if (request.getType().equals("firstName")) {
                        sql.append("and u.firstName = ? ");
                    }
                    if (request.getType().equals("lastName")) {
                        sql.append("and u.lastName = ? ");
                    }
                    if (request.getType().equals("fullName")) {
                        sql.append("and u.fullName = ? ");
                    }
                }

                sql.append("order by id desc ");

                if((request.getType() != null) && ("" != request.getType()) ) {
                    search = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(SearchWebUserDTO.class), request.getCreatedDateFrom(), request.getCreatedDateTo(), request.getSearchValue());
                } else {

                    search = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(SearchWebUserDTO.class), request.getCreatedDateFrom(), request.getCreatedDateTo());
                }
                }
        } catch (Exception e) {
            log.error("SearchWebUser", e);
        }
        return search;
    }

}
