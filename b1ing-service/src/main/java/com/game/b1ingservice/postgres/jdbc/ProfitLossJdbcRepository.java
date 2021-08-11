package com.game.b1ingservice.postgres.jdbc;

import com.game.b1ingservice.payload.admin.ProfitLossRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.deposithistory.ProfitAndLossRequest;
import com.game.b1ingservice.postgres.jdbc.dto.ProfitLoss;
import com.game.b1ingservice.postgres.jdbc.dto.SummaryDeposit;
import com.game.b1ingservice.postgres.jdbc.dto.SummaryWithdraw;
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
public class ProfitLossJdbcRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public List<ProfitLoss> depositProfit(ProfitLossRequest profitLossRequest, UserPrincipal principal) {
        List<ProfitLoss> deposit = new ArrayList<>();
        try {
            String sql = "select sum(d.amount) as deposit, sum(d.bonus_amount) as bonus, sum(d.amount + d.bonus_amount) as depositBonus " +
                    "from deposit_history d " +
                    "where d.created_date between TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') " +
                    "and TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') and d.agent_id = ?";

            deposit = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProfitLoss.class),
                    profitLossRequest.getListDateFrom(),profitLossRequest.getListDateTo(), principal.getAgentId());
        } catch (Exception e) {
            log.error("profitLoss", e);
        }
        return deposit;
    }
    public List<ProfitLoss> withdrawProfit(ProfitLossRequest profitLossRequest, UserPrincipal principal) {
        List<ProfitLoss> withdraw = new ArrayList<>();
        try {
            String sql = "select sum(wh.amount) as withdraw " +
                    "from withdraw_history wh " +
                    "where wh.created_date between TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') " +
                    "and TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') and wh.agent_id = ?";

            withdraw = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProfitLoss.class),
                    profitLossRequest.getListDateFrom(),profitLossRequest.getListDateTo(), principal.getAgentId());
        } catch (Exception e) {
            log.error("profitLoss", e);
        }
        return withdraw;
    }

    public List<SummaryDeposit> sumDeposit(ProfitAndLossRequest request, Long agentId) {

        List<SummaryDeposit> summaryDeposit = new ArrayList<>();
        try {
            String sql = "select sum(d.amount) as deposit, " +
                    "sum(d.bonus_amount) as bonus, " +
                    "mistake_type as mistakeType " +
                    "from deposit_history d " +
                    "where d.created_date between TO_TIMESTAMP(?, 'DD/MM/yyyy HH24:MI:SS') " +
                    "and TO_TIMESTAMP(?, 'DD/MM/yyyy HH24:MI:SS') " +
                    "and d.status = 'SUCCESS' " +
                    "and d.agent_id = ? group by mistake_type";
            summaryDeposit = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SummaryDeposit.class),
                    request.getCreatedDateFrom().concat(":00") ,request.getCreatedDateTo().concat(":59"), agentId);

        } catch (Exception e) {
            log.error("sumProfitLoss", e);
        }
        return summaryDeposit;

    }

    public List<SummaryWithdraw> sumWithdraw(ProfitAndLossRequest request, Long agentId) {

        List<SummaryWithdraw> summaryWithdraw = new ArrayList<>();

        try {
            String sql = "select sum(w.amount) as withdraw " +
                    "from withdraw_history w " +
                    "where w.created_date between TO_TIMESTAMP(? ,'DD/MM/yyyy HH24:MI:SS') " +
                    "and TO_TIMESTAMP(? ,'DD/MM/yyyy HH24:MI:SS') " +
                    "and w.status = 'SUCCESS' and mistake_type is null " +
                    "and w.agent_id = ?";
            summaryWithdraw = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SummaryWithdraw.class),
                    request.getCreatedDateFrom().concat(":00") ,request.getCreatedDateTo().concat(":59"), agentId);

        } catch (Exception e) {
            log.error("sumProfitLoss", e);
        }
        return summaryWithdraw;

    }
}
