package com.game.b1ingservice.postgres.jdbc;

import com.game.b1ingservice.payload.admin.ProfitLossRequest;
import com.game.b1ingservice.payload.admin.ProfitReportRequest;
import com.game.b1ingservice.payload.commons.UserPrincipal;
import com.game.b1ingservice.payload.deposithistory.ProfitAndLossRequest;
import com.game.b1ingservice.postgres.jdbc.dto.*;
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
                    "and TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') ";

            deposit = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProfitLoss.class), profitLossRequest.getListDateFrom(),profitLossRequest.getListDateTo());
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
                    "and TO_TIMESTAMP( ? ,'YYYY-MM-DD HH24:MI:SS') ";

            withdraw = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProfitLoss.class), profitLossRequest.getListDateFrom(),profitLossRequest.getListDateTo());
        } catch (Exception e) {
            log.error("profitLoss", e);
        }
        return withdraw;
    }

    public List<SummaryDeposit> sumDeposit(ProfitAndLossRequest request ) {

        List<SummaryDeposit> summaryDeposit = new ArrayList<>();

        try {

           StringBuilder sql = new StringBuilder();
           sql.append("select sum(amount) as deposit, ")
              .append("sum(bonus_amount) as bonus, ")
              .append("sum(amount + bonus_amount) as depositBonus ")
              .append("from deposit_history ")
              .append("where created_date between TO_TIMESTAMP(?, 'DD/MM/yyyy HH24:MI') ")
              .append("and TO_TIMESTAMP(?, 'DD/MM/yyyy HH24:MI');");

            summaryDeposit = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(SummaryDeposit.class), request.getCreatedDateFrom() ,request.getCreatedDateTo());

        } catch (Exception e) {
            log.error("sumProfitLoss", e);
        }
        return summaryDeposit;

    }

    public List<SummaryWithdraw> sumWithdraw(ProfitAndLossRequest request) {

        List<SummaryWithdraw> summaryWithdraw = new ArrayList<>();

        try {

            StringBuilder sql = new StringBuilder();
            sql.append("select sum(amount) as withdraw ")
                    .append("from withdraw_history ")
                    .append("where created_date between TO_TIMESTAMP(? ,'DD/MM/yyyy HH24:MI') ")
                    .append("and TO_TIMESTAMP(? ,'DD/MM/yyyy HH24:MI');");

            summaryWithdraw = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(SummaryWithdraw.class), request.getCreatedDateFrom() ,request.getCreatedDateTo());

        } catch (Exception e) {
            log.error("sumProfitLoss", e);
        }
        return summaryWithdraw;

    }
}
