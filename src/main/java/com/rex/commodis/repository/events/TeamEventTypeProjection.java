/** Spring Data JPA repository for TeamEventTypeProjection entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.events;

public interface TeamEventTypeProjection {
    Long getTeamEventTypeId();
    String getDisplayText();
    String getDescription();
}
