/** Service implementation for ReceiptNotes business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.rex.commodis.dto.ReceiptUpdateNotesRequestDto;
import com.rex.commodis.dto.ReceiptUpdateNotesResponseDto;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.receipts.Receipt;
import com.rex.commodis.model.receipts.ReceiptHistory;
import com.rex.commodis.repository.receipts.ReceiptApproverRepository;
import com.rex.commodis.repository.receipts.ReceiptHistoryRepository;
import com.rex.commodis.repository.receipts.ReceiptRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReceiptNotesServiceImpl implements ReceiptNotesService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptHistoryRepository receiptHistoryRepository;
    private final ReceiptApproverRepository receiptApproverRepository;

    @Override
    public ReceiptUpdateNotesResponseDto updateReceiptNotes(Long receiptId, Long rexUserId,
                                                            ReceiptUpdateNotesRequestDto notesRequest) {

        //TODO: Verify if the given rexUser can update notes - check approver or submitter
        if (!receiptApproverRepository.existsByReceiptIdAndRexUserId(receiptId, rexUserId)) {
            throw new AccessDeniedException(
                    "User does not have permission to change this receipt"
            );
        }

        // 🔹 1. Fetch receipt
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new NotFoundException(
                        "Receipt not found with ID: " + receiptId
                ));

        log.info("Fetched receipt {} — current internal note: {}", receiptId, receipt.getInternalNote());

        // 🔹 2. Update internal_note (receipts table)
        receipt.setInternalNote(notesRequest.getInternalNote());
        log.info("Set internal note to: {}", notesRequest.getInternalNote());

        // 🔹 3. Fetch active history row
        ReceiptHistory activeHistory = receiptHistoryRepository
                .findByReceiptIdAndActiveTrue(receiptId)
                .orElseThrow(() -> new NotFoundException("No active history found for receipt ID: " + receiptId));

        log.info("Fetched active history {} — current external note: {}", activeHistory.getId(), activeHistory.getExternalNote());

        // 🔹 4. Update external_note
        activeHistory.setExternalNote(notesRequest.getExternalNote());
        log.info("Set external note to: {}", notesRequest.getExternalNote());

        // 🔹 5. Save
        Receipt savedReceipt = receiptRepository.save(receipt);
        log.info("Saved receipt {} — internal note now: {}", savedReceipt.getId(), savedReceipt.getInternalNote());

        ReceiptHistory savedHistory = receiptHistoryRepository.save(activeHistory);
        log.info("Saved history {} — external note now: {}", savedHistory.getId(), savedHistory.getExternalNote());

        // 🔹 6. Return response
        return ReceiptUpdateNotesResponseDto.builder()
                .externalNote(savedHistory.getExternalNote())
                .internalNote(savedReceipt.getInternalNote())
                .build();
    }
}
