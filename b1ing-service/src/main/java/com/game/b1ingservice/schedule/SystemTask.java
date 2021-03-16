package com.game.b1ingservice.schedule;

import com.game.b1ingservice.service.MistakeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SystemTask {

    private MistakeService mistakeService;

    @Scheduled(cron = "${b1ing.schedule.mistake}")
    public void scheduleClearMistakeTask() {
        try {
            mistakeService.clearLimit();
        } catch (Exception e) {
            log.error("scheduleClearMistakeTask", e);
        }
    }
}
