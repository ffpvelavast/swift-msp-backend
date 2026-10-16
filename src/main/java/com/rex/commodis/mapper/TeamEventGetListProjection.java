/** Service interface defining TeamEventGetListProjection operations; implemented by TeamEventGetListProjectionImpl. */
package com.rex.commodis.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TeamEventGetListProjection {
    Long getTeamEventId();
    String getStatus();
    LocalDate getEventDate();
    String getEventTitle();
    String getSubmittedBy();
    String getProjectNumber();
    BigDecimal getTotalAmount();
}
