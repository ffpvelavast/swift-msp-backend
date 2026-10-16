/** Service implementation for TeamEventStatusUpdate business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.model.receipts.ReceiptHistory;
import com.rex.commodis.model.receipts.ReceiptStatus;
import com.rex.commodis.model.receipts.receiptToTeamEvent.ReceiptToTeamEvent;
import com.rex.commodis.repository.events.TeamEventApproverRepository;
import com.rex.commodis.repository.receipts.ReceiptHistoryRepository;
import com.rex.commodis.repository.receipts.ReceiptStatusRepository;
import com.rex.commodis.repository.receipts.ReceiptToTeamEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.rex.commodis.exceptions.ForbiddenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rex.commodis.dto.events.TeamEventStatusUpdateRequestDto;
import com.rex.commodis.dto.events.TeamEventStatusUpdateResponseDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.enums.TeamEventStatusEnum;
import com.rex.commodis.model.events.TeamEvent;
import com.rex.commodis.model.events.TeamEventHistory;
import com.rex.commodis.model.events.TeamEventStatus;
import com.rex.commodis.repository.events.TeamEventHistoryRepository;
import com.rex.commodis.repository.events.TeamEventRepository;
import com.rex.commodis.repository.events.TeamEventStatusRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamEventStatusUpdateServiceImpl implements TeamEventStatusUpdateService {

    private final TeamEventRepository teamEventRepository;
    private final TeamEventHistoryRepository teamEventHistoryRepository;
    private final TeamEventStatusRepository teamEventStatusRepository;
    private final TeamEventApproverRepository teamEventApproverRepository;
    private final ReceiptToTeamEventRepository receiptToTeamEventRepository;
    private final ReceiptHistoryRepository receiptHistoryRepository;
    private final ReceiptStatusRepository receiptStatusRepository;


    private static ReceiptStatusEnum toReceiptStatus(
            TeamEventStatusEnum teamEventStatus
    ) {
        return ReceiptStatusEnum.valueOf(teamEventStatus.name());
    }

    private static final Set<TeamEventStatusEnum> ALLOWED_UI_STATUSES = Set.of(
            TeamEventStatusEnum.APPROVED,
            TeamEventStatusEnum.REJECTED
    );


    @Override
    @Transactional
    public TeamEventStatusUpdateResponseDto updateTeamEventStatus(
            Long teamEventId,
            TeamEventStatusUpdateRequestDto requestDto,
            Long rexUserId
    ) {
        log.info("Team event status update requested — teamEventId={}, status={}, rexUserId={}",teamEventId, requestDto.getStatus(), rexUserId);

        boolean exist = teamEventApproverRepository
                .existsByTeamEventIdAndRexUserId(teamEventId, rexUserId);

        log.info("exist={}", exist);
        //TODO need to find if the rexuser has permission to change the team event status
        if (!exist) {
            throw new ForbiddenException(
                    "User does not have permission to change the status"
            );
        }

        //  Only APPROVED and REJECTED allowed via this endpoint
        validateAllowedStatus(requestDto.getStatus());

        // Verify the team event exists
        verifyTeamEventExists(teamEventId);

        //  Resolve enum → TeamEventStatus entity (gets PK)
        TeamEventStatus targetStatus = resolveTeamEventStatus(requestDto.getStatus());

        // Fetch the current active history row
        TeamEventHistory currentActiveHistory = teamEventHistoryRepository
                .findActiveByTeamEventId(teamEventId)
                .orElseThrow(() -> new NotFoundException(
                        "No active status found for teamEventId=" + teamEventId
                ));

        verifyStatusIsNotFinal(teamEventId);

        //  Prevent setting the same status twice
        if (currentActiveHistory.getStatus().getId().equals(targetStatus.getId())) {
            throw new BadRequestException(
                    "Team event already has status '" + requestDto.getStatus().name() + "'"
            );
        }

        //  Deactivate the current active history row
        //    Direct JPQL UPDATE
        int deactivatedCount = teamEventHistoryRepository
                .deactivateCurrentActiveStatus(teamEventId);

        log.debug("Deactivated {} active history row(s) for teamEventId={}",deactivatedCount, teamEventId);

        //  Insert new active history row
        TeamEventHistory newHistoryEntry = buildNewHistoryEntry(teamEventId,rexUserId,targetStatus);
        teamEventHistoryRepository.save(newHistoryEntry);

        log.info("Team event status updated — teamEventId={}, newStatus={}, creatorId={}",
                teamEventId, requestDto.getStatus().name(), rexUserId);


        updateReceiptStatusesForTeamEvent(
                teamEventId,
                requestDto.getStatus(),
                rexUserId
        );

        //  Return response
        return TeamEventStatusUpdateResponseDto.builder()
                .teamEventId(teamEventId)
                .status(requestDto.getStatus().name())
                .build();
    }

    // Private helpers

    private void updateReceiptStatusesForTeamEvent(
            Long teamEventId,
            TeamEventStatusEnum teamEventStatus,
            Long rexUserId
    ) {


        List<ReceiptToTeamEvent> mappings =
                receiptToTeamEventRepository
                        .findByTeamEventId(teamEventId);


        if (mappings.isEmpty()) {

            log.info(
                    "No receipt mapping found for teamEventId={} — skipping receipt sync",
                    teamEventId
            );

            return;
        }




        for (ReceiptToTeamEvent mapping : mappings) {

            Long receiptId = mapping.getReceiptId();

            log.info(
                    "Receipt mapping found — teamEventId={}, receiptId={}",
                    teamEventId,
                    receiptId
            );


            ReceiptStatusEnum targetReceiptStatusEnum =
                    toReceiptStatus(teamEventStatus);


            ReceiptStatus targetReceiptStatus =
                    receiptStatusRepository
                            .findByStatus(targetReceiptStatusEnum)
                            .get();


            receiptHistoryRepository.deactivateCurrentActiveStatus(receiptId);



            ReceiptHistory newReceiptHistory = ReceiptHistory.builder()
                    .receiptId(receiptId)
                    .creatorId(rexUserId)
                    .receiptStatusId(targetReceiptStatus.getId())
                    .active(true)
                    .externalNote(null)
                    .build();


            receiptHistoryRepository.save(newReceiptHistory);

            log.info(
                    "Receipt status synced — receiptId={}, newStatus={}, creatorId={}",
                    receiptId,
                    targetReceiptStatusEnum.name(),
                    rexUserId
            );
        }
    }


    private void verifyStatusIsNotFinal(Long teamEventId) {
        teamEventHistoryRepository.findActiveStatusCodeByTeamEventId(teamEventId)
                .ifPresent(current -> {
                    if (ALLOWED_UI_STATUSES.contains(current)) {
                        throw new BadRequestException(
                                "Current Team event status is '" + current.name() + "' and cannot be changed."
                        );
                    }
                });
    }

    private void validateAllowedStatus(TeamEventStatusEnum status) {
        if (!ALLOWED_UI_STATUSES.contains(status)) {
            throw new BadRequestException(
                    "Invalid status '" + status.name() + "' for this endpoint. " +
                            "Allowed values: APPROVED, REJECTED"
            );
        }
    }

    private void verifyTeamEventExists(Long teamEventId) {
        if (!teamEventRepository.existsById(teamEventId)) {
            throw new NotFoundException("Team event not found with ID: " + teamEventId);
        }
    }

    private TeamEventStatus resolveTeamEventStatus(TeamEventStatusEnum statusEnum) {
        return teamEventStatusRepository
                .findByStatusCode(statusEnum)
                .get();
    }

    private TeamEventHistory buildNewHistoryEntry(
            Long teamEventId,
            Long creatorId,
            TeamEventStatus status
    ) {
        TeamEventHistory history = new TeamEventHistory();

        history.setTeamEventId(teamEventId);
        history.setCreatorId(creatorId);
        history.setStatusId(status.getId()); // if Short

        history.setCreatedAt(OffsetDateTime.now());
        history.setActive(true);
        history.setExternalNote(null);

        return history;
    }

}

