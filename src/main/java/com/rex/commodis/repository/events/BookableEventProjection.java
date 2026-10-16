/** Spring Data JPA repository for BookableEventProjection entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.events;

public interface BookableEventProjection {
    String getEventSource();
    Long getEventId();
    String getEventTitle();
    Boolean getInheritParticipants();
}
