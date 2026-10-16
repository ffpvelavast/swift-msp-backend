/** Service implementation for InvoiceResponsibility business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service.invoiceServices;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.rex.commodis.dto.invoiceDtos.*;
import com.rex.commodis.exceptions.ForbiddenException;
import com.rex.commodis.model.invoices.InvoiceResponsibility;
import com.rex.commodis.repository.invoices.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class InvoiceResponsibilityServiceImpl implements InvoiceResponsibilityService {

    private final InvoiceResponsibilityRepository invoiceResponsibilityRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    @Transactional
    public void validateUserResponsibility(Long invoiceId, Long rexUserId) {

        boolean isActive = invoiceResponsibilityRepository
                .existsByInvoiceIdAndDelegateIdAndIsActiveTrue(invoiceId, rexUserId);

        if (!isActive) {
            throw new ForbiddenException(
                    "User is not authorized or responsibility is inactive for this invoice"
            );
        }
    }


    @Override
    @Transactional
    public InvoiceDelegateResponseDto delegateInvoice(Long invoiceId,
                                                      InvoiceDelegateRequestDto requestDto,
                                                      Long rexUserId) {

        OffsetDateTime now = OffsetDateTime.now();

        // Get current active responsibility
        InvoiceResponsibility currentResponsibilityRecord =
                invoiceResponsibilityRepository
                        .findByInvoiceIdAndDelegateIdAndIsActiveTrue(invoiceId, rexUserId)
                        .orElseThrow(() ->
                                new ForbiddenException("No active responsibility for invoice found for user: " + rexUserId)
                        );

        // Close current responsibility
        currentResponsibilityRecord.setResponsibleThrough(now);
        currentResponsibilityRecord.setIsActive(false);
        invoiceResponsibilityRepository.save(currentResponsibilityRecord);

        // Create new responsibility
        InvoiceResponsibility newResponsibilityRecord = InvoiceResponsibility.builder()
                .invoiceId(invoiceId)
                .rexUserId(rexUserId)
                .delegateId(requestDto.getDelegateId())
                .delegatedAt(now)
                .responsibleFrom(now)
                .responsibleThrough(OffsetDateTime.parse("2999-12-31T23:59:59+00:00"))
                .isActive(true)
                .build();

        invoiceResponsibilityRepository.save(newResponsibilityRecord);

        return new InvoiceDelegateResponseDto(
                true,
                "Invoice delegated successfully"
        );
    }

    public void createResponsibility(Long invoiceId, Long rexUserId, OffsetDateTime now) {
        //Create and Save new InvoiceResponsibility
        InvoiceResponsibility invoiceResponsibility = new InvoiceResponsibility();

        invoiceResponsibility.setInvoiceId(invoiceId);
        invoiceResponsibility.setRexUserId(null);
        invoiceResponsibility.setDelegateId(rexUserId);
        invoiceResponsibility.setDelegatedAt(now);
        invoiceResponsibility.setRemarks(null);
        invoiceResponsibility.setResponsibleFrom(now);
        invoiceResponsibility.setResponsibleThrough(OffsetDateTime.parse("2999-12-31T23:59:59+00:00"));

        invoiceResponsibilityRepository.save(invoiceResponsibility);
    }

}


