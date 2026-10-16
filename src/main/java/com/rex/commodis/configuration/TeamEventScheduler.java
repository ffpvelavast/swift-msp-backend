package com.rex.commodis.configuration;

import com.rex.commodis.service.TeamEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        prefix = "teamevent.job",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class TeamEventScheduler {

    private final TeamEventService teamEventService;

    private static final Logger log = LoggerFactory.getLogger(TeamEventScheduler.class);

    public TeamEventScheduler(TeamEventService service) {
        this.teamEventService = service;
    }

    // Runs daily at 2 PM ISTs
    @Scheduled(cron = "${teamevent.job.cron}", zone = "${teamevent.job.zone}")
    public void runDailyJob() {

        log.info("Team Event Cron Started");
        try {
            teamEventService.processAndReleasePastTeamEvents();
        } catch (Exception e) {
            log.error("Team Event Cron FAILED", e);
        }
        log.info("Team Event Cron Completed");
    }
}