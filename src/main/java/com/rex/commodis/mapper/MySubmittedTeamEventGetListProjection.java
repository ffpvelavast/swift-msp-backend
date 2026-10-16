package com.rex.commodis.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface MySubmittedTeamEventGetListProjection {

    Long getTeamEventId();

    String getStatus();

    LocalDate getEventDate();

    String getEventTitle();

    String getProjectNumber();

    BigDecimal getTotalAmount();
}