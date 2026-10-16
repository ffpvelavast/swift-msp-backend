package com.rex.commodis.repository.events;

import java.time.LocalDate;

public interface DashboardTeamEventListProjection {
    Long getTeamEventId();

    String getEventName();

    LocalDate getEventDate();

    String getStatus();
}
