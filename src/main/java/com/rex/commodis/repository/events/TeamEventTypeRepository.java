/** Spring Data JPA repository for TeamEventType entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.events;

import com.rex.commodis.model.events.TeamEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamEventTypeRepository extends JpaRepository<TeamEventType, Long> {

    @Query(value = """
        SELECT
            team_event_type_id AS "teamEventTypeId",
            displaytext AS "displayText",
            description AS "description"
        FROM events.select_team_eventtype(:platformId,:locale)
    """, nativeQuery = true)
    List<TeamEventTypeProjection> findTeamEventTypes(
            @Param("platformId") Long platformId,
            @Param("locale") String locale
    );
}
