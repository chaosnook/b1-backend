package com.game.b1ingservice.schedule;

import com.game.b1ingservice.service.MistakeService;
import com.game.b1ingservice.service.WebUserService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SystemTask {

    @Autowired
    private MistakeService mistakeService;

    @Autowired
    private WebUserService webUserService;

    @Scheduled(cron = "${b1ing.schedule.mistake}")
    @SchedulerLock(name = "scheduleClearMistakeTask",
            lockAtLeastForString = "PT10M", lockAtMostForString = "PT10M")
    public void scheduleClearMistakeTask() {
        try {
            mistakeService.clearLimit();
            webUserService.clearCountWithdraw();
        } catch (Exception e) {
            log.error("scheduleClearMistakeTask", e);
        }
    }
}
