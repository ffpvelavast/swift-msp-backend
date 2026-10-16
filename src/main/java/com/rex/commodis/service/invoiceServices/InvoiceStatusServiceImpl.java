package com.rex.commodis.service.invoiceServices;

import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.enums.InvoiceStatusEnum;
import com.rex.commodis.model.invoices.InvoiceHistory;
import com.rex.commodis.model.invoices.InvoiceStatus;
import com.rex.commodis.repository.invoices.InvoiceHistoryRepository;
import com.rex.commodis.repository.invoices.InvoiceStatusRepository;
import com.rex.commodis.service.invoiceServices.InvoiceStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class InvoiceStatusServiceImpl implements InvoiceStatusService {

    private final InvoiceStatusRepository invoiceStatusRepository;
    private final InvoiceHistoryRepository invoiceHistoryRepository;


    // Implements: InvoiceStatusService.getByStatus(String)
    @Override
    @Transactional(readOnly = true)
    public InvoiceStatus getByStatus(String status) {
        InvoiceStatusEnum statusEnum = parseStatus(status);
        return invoiceStatusRepository
                .findByStatus(statusEnum)
                .orElseThrow(() -> new NotFoundException(
                        "Invoice status definition not found: " + status));
    }


    // Implements: InvoiceStatusService.createOrUpdateDraftStatus(Long, Long)

    @Override
    @Transactional
    public void createOrUpdateDraftStatus(Long invoiceId, Long rexUserId) {

        boolean alreadyDraft =
                invoiceHistoryRepository
                        .findActiveByInvoiceIdAndStatus(
                                invoiceId,
                                InvoiceStatusEnum.DRAFT)
                        .isPresent();

        if (alreadyDraft) {
            return;
        }


        InvoiceStatus draftStatusDef = invoiceStatusRepository
                .findByStatus(InvoiceStatusEnum.DRAFT)
                .get();


        invoiceHistoryRepository
                .deactivateCurrentActiveHistory(invoiceId);

        invoiceHistoryRepository
                .findActiveByInvoiceIdAndStatus(invoiceId, InvoiceStatusEnum.DRAFT)
                .ifPresentOrElse(
                        existing -> {
                            // Active DRAFT already exists — no-op
                        },
                        () -> invoiceHistoryRepository.save(
                                InvoiceHistory.builder()
                                        .invoiceId(invoiceId)
                                        .statusId(draftStatusDef.getId())
                                        .statusCreator(String.valueOf(rexUserId))
                                        .active(true)
                                        .build()
                        )
                );
    }

    // Private: safe String → InvoiceStatusEnum conversion

    private InvoiceStatusEnum parseStatus(String status) {
        try {
            return InvoiceStatusEnum.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                    "Invalid invoice status: '" + status + "'. " +
                            "Allowed values: " + Arrays.toString(InvoiceStatusEnum.values()));
        }
    }
}