package com.game.b1ingservice.schedule;

import com.game.b1ingservice.service.MistakeService;
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

    @Scheduled(cron = "${b1ing.schedule.mistake}")
    @SchedulerLock(name = "scheduleClearMistakeTask",
            lockAtLeastForString = "PT10M", lockAtMostForString = "PT10M")
    public void scheduleClearMistakeTask() {
        try {
            mistakeService.clearLimit();
        } catch (Exception e) {
            log.error("scheduleClearMistakeTask", e);
        }
    }
}
