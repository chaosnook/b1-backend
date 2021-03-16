package com.game.b1ingservice.schedule;

import com.game.b1ingservice.payload.amb.*;
import com.game.b1ingservice.postgres.entity.Agent;
import com.game.b1ingservice.postgres.entity.WinLoseHistory;
import com.game.b1ingservice.postgres.repository.WinLoseHistoryRepository;
import com.game.b1ingservice.service.AMBService;
import com.game.b1ingservice.service.AgentService;
import com.game.b1ingservice.service.WalletService;
import com.game.b1ingservice.service.WebUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class TurnOverCheckTask {

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private AMBService ambService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WinLoseHistoryRepository winLoseHistoryRepository;

    @Scheduled(cron = "${b1ing.schedule.turnover}")
    public void scheduleTurnOverCheckTask() {
        try {
            // Get start end date
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            Date start;
//            Date end;
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//            if (hour < 11) {
//                start = atAMBStartOfDay(-1);
//                end = atAmbEndOfDay(0);
//            } else {
//                start = atAMBStartOfDay(0);
//                end = atAmbEndOfDay(+1);
//            }

            List<Agent> agents = agentService.getAllAgent();
            for (Agent agent : agents) {
                List<WinLoseReq> userList = webUserService.getAllUser(agent.getId());

                for (WinLoseReq winLoseReq : userList) {
                    try {
                        log.info("before check turnover user {}", winLoseReq);
                        AmbResponse<WinLoseResponse> response = ambService.getWinLose(winLoseReq, agent);
                        if (response.getCode() == 0) {

                            // sum amount
                            WinLoseResponse winLoseResponse = response.getResult();
                            WinLoseDataList dataList = winLoseResponse.getData();

                            BigDecimal amount = BigDecimal.ZERO;
                            WinLoseData FOOTBALL = dataList.getFOOTBALL();
                            amount = amount.add(FOOTBALL.getAmount());
                            WinLoseData STEP = dataList.getSTEP();
                            amount = amount.add(STEP.getAmount());
                            WinLoseData PARLAY = dataList.getPARLAY();
                            amount = amount.add(PARLAY.getAmount());
                            WinLoseData GAME = dataList.getGAME();
                            amount = amount.add(GAME.getAmount());
                            WinLoseData CASINO = dataList.getCASINO();
                            amount = amount.add(CASINO.getAmount());
                            WinLoseData LOTTO = dataList.getLOTTO();
                            amount = amount.add(LOTTO.getAmount());
                            WinLoseData M2 = dataList.getM2();
                            amount = amount.add(M2.getAmount());
                            WinLoseData MULTI_PLAYER = dataList.getMULTI_PLAYER();
                            amount = amount.add(MULTI_PLAYER.getAmount());


                            WinLoseHistory history = winLoseHistoryRepository.findFirstByUserIdOrderByIdDesc(winLoseReq.getId());

                            BigDecimal beforeAmount = BigDecimal.ZERO;
                            if (history != null) {
                                beforeAmount = history.getAmount();
                            }

                            BigDecimal afterAmount = amount.subtract(beforeAmount);
                            // Create new WinLoseHistory
                            WinLoseHistory newHis = new WinLoseHistory();

                            newHis.setAmount(amount);
                            newHis.setBeforeAmount(beforeAmount);
                            newHis.setAfterAmount(afterAmount);

                            newHis.setAgent(agent);
                            newHis.setLastDate(new Date());
                            newHis.setUserId(winLoseReq.getId());
                            winLoseHistoryRepository.save(newHis);

                            log.info("check turnover user {} , before {} , amount {} , after {}", winLoseReq.getUsername(), beforeAmount, amount, afterAmount);

                            //Toto last after - amount แล้วไปลย turnover ใน wallet
                            if (afterAmount.compareTo(BigDecimal.ZERO) > 0) {
                                walletService.minusTurnOver(winLoseReq.getId(), afterAmount);
                            }

                        }

                    } catch (Exception e) {
                        log.error("scheduleTurnOverCheckTask", e);
                    }
                }
            }

        } catch (Exception e) {
            log.error("scheduleTurnOverCheckTask", e);
        }

    }

}
