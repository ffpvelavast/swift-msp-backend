/** Spring Data JPA repository for TeamEventApprover entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.events;

import com.rex.commodis.model.enums.TeamEventStatusEnum;
import com.rex.commodis.model.events.TeamEventApprover.TeamEventApprover;
import com.rex.commodis.model.events.TeamEventApprover.TeamEventApproverId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamEventApproverRepository
        extends JpaRepository<TeamEventApprover, TeamEventApproverId> {

    boolean existsByTeamEventIdAndRexUserId(Long teamEventId, Long rexUserId);

    List<TeamEventApprover> findByTeamEventId(Long teamEventId);

    @Query("""
    SELECT COUNT(DISTINCT tea.teamEvent.id)
    FROM TeamEventApprover tea
    WHERE tea.rexUser.id = :rexUserId
      AND EXISTS (
          SELECT teh.id
          FROM TeamEventHistory teh
          WHERE teh.teamEvent.id = tea.teamEvent.id
            AND teh.active = true
            AND teh.status.statusCode IN :statuses
      )
    """)
    Long countApproverTeamEventsByStatuses(
            @Param("rexUserId") Long rexUserId,
            @Param("statuses") List<TeamEventStatusEnum> statuses
    );
}