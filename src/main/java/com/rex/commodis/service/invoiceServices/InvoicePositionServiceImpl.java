package com.rex.commodis.service.invoiceServices;

import com.rex.commodis.dto.invoiceDtos.SavePositionDataDto;
import com.rex.commodis.dto.invoiceDtos.SavePositionsRequestDto;
import com.rex.commodis.dto.invoiceDtos.SavePositionsResponseDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.model.invoices.InvoicePositionsBeneficiary;
import com.rex.commodis.model.invoices.InvoicePositionsGuest;
import com.rex.commodis.repository.invoices.InvoicePositionsBeneficiaryRepository;
import com.rex.commodis.repository.invoices.InvoicePositionsGuestRepository;
import com.rex.commodis.repository.invoices.InvoicePositionRepository;
import com.rex.commodis.repository.invoices.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InvoicePositionServiceImpl implements InvoicePositionService {

    private final InvoicePositionRepository invoicePositionRepository;
    private final InvoicePositionsBeneficiaryRepository invoicePositionsBeneficiaryRepository;
    private final InvoicePositionsGuestRepository invoicePositionsGuestRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceResponsibilityService invoiceResponsibilityService;

    @Override
    public Set<String> findDistinctProjectNumbers(Long invoiceId) {
        return invoicePositionRepository.findDistinctProjectNumbers(invoiceId);
    }

    @Override
    public boolean existsByProjectNumberId(Long projectNumberId) {
        return invoicePositionRepository.existsByProjectNumberId(projectNumberId);
    }

    @Override
    @Transactional
    public SavePositionsResponseDto savePositions(Long invoiceId, Long rexUserId, SavePositionsRequestDto requestDto) {
        invoiceResponsibilityService.validateUserResponsibility(invoiceId, rexUserId);

        if (requestDto.getNote() != null) {
            invoiceRepository.updateNote(invoiceId, requestDto.getNote());
        }

        List<SavePositionDataDto> positions = requestDto.getInvoicePositionData();
        if (positions == null || positions.isEmpty()) {
            return new SavePositionsResponseDto(invoiceId);
        }

        for (SavePositionDataDto positionData : positions) {
            if (!invoicePositionRepository.existsByIdAndInvoiceId(positionData.getInvoicePositionId(), invoiceId)) {
                throw new BadRequestException(
                        "Invoice position " + positionData.getInvoicePositionId() + " does not belong to invoice " + invoiceId);
            }
        }

        for (SavePositionDataDto positionData : positions) {
            Long positionId = positionData.getInvoicePositionId();

            if (positionData.getTaxReasonId() != null) {
                invoicePositionRepository.updateTaxReasonClientId(positionId, positionData.getTaxReasonId());
            }

            if (positionData.getBeneficiaryIds() != null) {
                List<Long> requested = positionData.getBeneficiaryIds();
                List<Long> existing = invoicePositionsBeneficiaryRepository.findActiveIdsByInvoicePositionId(positionId);

                Set<Long> requestedSet = new HashSet<>(requested);
                Set<Long> existingSet = new HashSet<>(existing);

                List<Long> toRemove = existing.stream().filter(id -> !requestedSet.contains(id)).toList();
                List<Long> toAdd = requested.stream().filter(id -> !existingSet.contains(id)).toList();

                if (!toRemove.isEmpty()) {
                    invoicePositionsBeneficiaryRepository.softDeleteByPositionIdAndBeneficiaryIds(positionId, toRemove);
                }
                if (!toAdd.isEmpty()) {
                    List<InvoicePositionsBeneficiary> newBeneficiaries = toAdd.stream()
                            .map(beneficiaryId -> InvoicePositionsBeneficiary.builder()
                                    .invoicePositionId(positionId)
                                    .beneficiaryId(beneficiaryId)
                                    .isDeleted(false)
                                    .build())
                            .toList();
                    invoicePositionsBeneficiaryRepository.saveAll(newBeneficiaries);
                }
            }

            if (positionData.getGuestIds() != null) {
                List<Long> requested = positionData.getGuestIds();
                List<Long> existing = invoicePositionsGuestRepository.findActiveIdsByInvoicePositionId(positionId);

                Set<Long> requestedSet = new HashSet<>(requested);
                Set<Long> existingSet = new HashSet<>(existing);

                List<Long> toRemove = existing.stream().filter(id -> !requestedSet.contains(id)).toList();
                List<Long> toAdd = requested.stream().filter(id -> !existingSet.contains(id)).toList();

                if (!toRemove.isEmpty()) {
                    invoicePositionsGuestRepository.softDeleteByPositionIdAndGuestIds(positionId, toRemove);
                }
                if (!toAdd.isEmpty()) {
                    List<InvoicePositionsGuest> newGuests = toAdd.stream()
                            .map(guestId -> InvoicePositionsGuest.builder()
                                    .invoicePositionId(positionId)
                                    .guestId(guestId)
                                    .isDeleted(false)
                                    .build())
                            .toList();
                    invoicePositionsGuestRepository.saveAll(newGuests);
                }
            }
        }

        return new SavePositionsResponseDto(invoiceId);
    }
}
