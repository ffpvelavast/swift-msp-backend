/** Spring Data JPA repository for TeamEventHistory entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.enums.TeamEventStatusEnum;
import com.rex.commodis.model.events.TeamEventHistory;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamEventHistoryRepository extends JpaRepository<TeamEventHistory, Long> {

    List<TeamEventHistory> findByTeamEventId(Long teamEventId);

    Optional<TeamEventHistory> findFirstByTeamEventIdAndActiveTrueOrderByCreatedAtDesc(Long teamEventId);


    @Query("""
            SELECT teh
            FROM TeamEventHistory teh
            WHERE teh.teamEvent.id = :teamEventId
              AND teh.active = true
            """)
    Optional<TeamEventHistory> findActiveByTeamEventId(@Param("teamEventId") Long teamEventId);


    @Query("""
            SELECT ts.statusCode
            FROM TeamEventHistory teh
            JOIN teh.status ts
            WHERE teh.teamEvent.id = :teamEventId
              AND teh.active = true
            """)
    Optional<TeamEventStatusEnum> findActiveStatusCodeByTeamEventId(@Param("teamEventId") Long teamEventId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE TeamEventHistory teh
            SET teh.active = false
            WHERE teh.teamEvent.id= :teamEventId
              AND teh.active = true
            """)
    int deactivateCurrentActiveStatus(@Param("teamEventId") Long teamEventId);


    @Query(value = """
        SELECT
            te.id AS teamEventId,
            te.event_title::text AS eventName,
            te.event_date AS eventDate,
            tes.display_text ->> :locale AS status
        FROM events.team_event_approver tea
    
        JOIN events.team_event_history teh
            ON teh.teamevent_id = tea.teamevent_id
    
        JOIN events.team_event te
            ON te.id = teh.teamevent_id
    
        JOIN events.team_event_status tes
            ON tes.id = teh.status_id
    
        WHERE tea.rexuser_id = :rexUserId
          AND teh.active = true
          AND tes.status_code IN (
              'SUBMITTED',
              'RESUBMITTED',
              'CORRECTION_REQUESTED'
          )
    
        ORDER BY te.created_at DESC
        LIMIT 5
    """, nativeQuery = true)
    List<DashboardTeamEventListProjection> findTeamEventsListForApprover(
            @Param("rexUserId") Long rexUserId,
            @Param("locale") String locale
    );
}