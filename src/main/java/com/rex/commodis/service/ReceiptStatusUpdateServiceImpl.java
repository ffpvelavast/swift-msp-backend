/** Service implementation for ReceiptStatusUpdate business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.exceptions.ForbiddenException;
import com.rex.commodis.model.enums.ReceiptContextEnum;
import com.rex.commodis.model.receipts.receiptApprovals.ReceiptApprovalId;
import com.rex.commodis.model.receipts.receiptApprovals.ReceiptApprovals;
import com.rex.commodis.repository.RexUserRepository;
import com.rex.commodis.repository.receipts.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rex.commodis.dto.receipts.ReceiptStatusUpdateRequestDto;
import com.rex.commodis.dto.receipts.ReceiptStatusUpdateResponseDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.model.receipts.ReceiptHistory;
import com.rex.commodis.model.receipts.ReceiptStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptStatusUpdateServiceImpl implements ReceiptStatusUpdateService {

    private static final Set<ReceiptStatusEnum> FINAL_STATUSES = Set.of(
            ReceiptStatusEnum.APPROVED,
            ReceiptStatusEnum.REJECTED
    );

    private final ReceiptRepository receiptRepository;
    private final ReceiptHistoryRepository receiptHistoryRepository;
    private final ReceiptStatusRepository receiptStatusRepository;
    private final ReceiptApproverRepository receiptApproverRepository;
    private final ReceiptApprovalsRepository receiptApprovalsRepository;
    private final ReceiptToTeamEventRepository receiptToTeamEventRepository;
    private final ReceiptToCorporateEventRepository receiptToCorporateEventRepository;
    private final RexUserRepository rexUserRepository;


    @Override
    @Transactional
    public ReceiptStatusUpdateResponseDto updateReceiptStatus(
            ReceiptStatusUpdateRequestDto receiptStatusUpdateRequestDto,
            Long receiptId,
            Long rexUserId
    ) {

        // Verify receipt exists
        verifyReceiptExists(receiptId);
        
        //TODO need to find if the rexuser has permission to change the receipt status
        if (!receiptApproverRepository.existsByReceiptIdAndRexUserId(receiptId, rexUserId)) {
            throw new ForbiddenException(
                    "User does not have permission to change this receipt"
            );
        }

        ReceiptStatusEnum statusEnum = receiptStatusUpdateRequestDto.getStatus();

        // Resolve incoming status → entity
        ReceiptStatus targetReceiptStatus = resolveReceiptStatus(statusEnum);

        // 🔹 Fetch current active status
        ReceiptHistory dbReceiptHistory = receiptHistoryRepository
                .findActiveByReceiptId(receiptId)
                .orElseThrow(() -> new NotFoundException(
                        "No active status found for receiptId=" + receiptId
                ));

        verifyStatusIsNotFinal(receiptId);

        // Deactivate current active row
        int deactivatedCount = receiptHistoryRepository
                .deactivateCurrentActiveStatus(receiptId);

        // Insert new active row, preserving the external note
        ReceiptHistory newHistoryEntry = buildNewHistoryEntry(
                receiptId,
                rexUserId,
                targetReceiptStatus.getId()
        );
        receiptHistoryRepository.save(newHistoryEntry);

        if (statusEnum == ReceiptStatusEnum.APPROVED) {
            createReceiptApproval(receiptId, rexUserId);
        }


        return ReceiptStatusUpdateResponseDto.builder()
                .receiptId(receiptId)
                .creatorId(rexUserId)
                .status(statusEnum.name())
                .build();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Private helpers
    // ═══════════════════════════════════════════════════════════════════════


    private void verifyStatusIsNotFinal(Long receiptId) {
        receiptHistoryRepository.findActiveStatusCodeByReceiptId(receiptId)
                .ifPresent(current -> {
                    if (FINAL_STATUSES.contains(current)) {
                        throw new BadRequestException(
                                "Current Receipt status is '" + current.name() + "' and cannot be changed."
                        );
                    }
                });
    }

    private ReceiptStatus resolveReceiptStatus(ReceiptStatusEnum statusEnum) {
        return receiptStatusRepository
                .findByStatus(statusEnum)
                .orElseThrow(() -> new NotFoundException(
                        "Receipt status '" + statusEnum.name() + "' is not configured in the database."
                ));
    }
	/**
     * Confirms the receipt record exists before any write operations.
     * Uses existsById — single COUNT query, no entity hydration.
     */
    private void verifyReceiptExists(Long receiptId) {
        if (!receiptRepository.existsById(receiptId)) {
            throw new NotFoundException("Receipt not found with ID: " + receiptId);
        }
    }




    private ReceiptHistory buildNewHistoryEntry(
            Long receiptId,
            Long creatorId,
            Short receiptStatusId
    ) {
        return ReceiptHistory.builder()
                .receiptId(receiptId)
                .creatorId(creatorId)
                .receiptStatusId(receiptStatusId)
                .active(true)
                .externalNote(null)
                .build();
    }

    private void createReceiptApproval(Long receiptId, Long approverId) {

        ReceiptContextEnum receiptContext;

        if (receiptToTeamEventRepository.existsByReceiptId(receiptId)) {
            receiptContext = ReceiptContextEnum.TEAM_EVENT;

        } else if (receiptToCorporateEventRepository.existsByReceiptId(receiptId)) {
            receiptContext = ReceiptContextEnum.CORPORATE_EVENT;

        } else {
            receiptContext = ReceiptContextEnum.STANDALONE;
        }

        ReceiptApprovalId approvalId = new ReceiptApprovalId(
                receiptId,
                approverId
        );

        ReceiptApprovals receiptApproval = new ReceiptApprovals();

        receiptApproval.setId(approvalId);

        receiptApproval.setReceipt(
                receiptRepository.getReferenceById(receiptId)
        );


        receiptApproval.setApprover(
                rexUserRepository.getReferenceById(approverId)
        );

        receiptApproval.setReceiptContext(receiptContext.name());
        receiptApproval.setCreatedAt(LocalDateTime.now());

        receiptApprovalsRepository.save(receiptApproval);
    }
}