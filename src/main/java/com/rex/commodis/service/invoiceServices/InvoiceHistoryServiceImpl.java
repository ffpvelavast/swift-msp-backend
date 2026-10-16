/** Service implementation for InvoiceHistory business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service.invoiceServices;

import com.rex.commodis.exceptions.ConflictException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.enums.InvoiceStatusEnum;
import com.rex.commodis.model.invoices.*;
import com.rex.commodis.repository.invoices.InvoiceHistoryRepository;
import com.rex.commodis.repository.invoices.InvoiceStatusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceHistoryServiceImpl implements InvoiceHistoryService{

    private final InvoiceStatusRepository invoiceStatusRepository;
    private final InvoiceHistoryRepository invoiceHistoryRepository;

    @Transactional
    public void validateDuplicateStatus(Long invoiceId, InvoiceStatusEnum status) {

        Optional<InvoiceHistory> latestHistory =
                invoiceHistoryRepository.findTopByInvoiceIdOrderByCreatedAtDesc(invoiceId);

        if (latestHistory.isPresent() &&
                latestHistory.get().getStatus().getStatus() == status) {

            throw new ConflictException("Invoice already has this status");
        }
    }

    @Transactional
    public void createStatus(Long invoiceId, InvoiceStatusEnum status, String fullName) {

        //Find status_id from invoice_status
        Long statusId = invoiceStatusRepository.findByStatus(status)
                .map(InvoiceStatus::getId)
                .orElseThrow(() -> new NotFoundException("Status value not found: " + status));

        //Build new history record
        InvoiceHistory newHistory = InvoiceHistory.builder()
                .invoiceId(invoiceId)
                .statusId(statusId)
                .statusCreator(fullName)
                .active(true)
                .build();

        invoiceHistoryRepository.save(newHistory);
    }
}
