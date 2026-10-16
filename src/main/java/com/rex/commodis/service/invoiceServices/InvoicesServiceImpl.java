/** Service implementation for Invoices business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service.invoiceServices;

import java.time.LocalDate;
import java.util.*;

import com.rex.commodis.dto.*;
import com.rex.commodis.dto.invoiceDtos.*;
import com.rex.commodis.dto.invoices.InvoiceEventDto;
import com.rex.commodis.mapper.AssignedBeneficiaryMapper;
import com.rex.commodis.mapper.invoices.*;
import com.rex.commodis.model.*;
import com.rex.commodis.model.invoices.*;
import com.rex.commodis.model.events.CorporateEvent;
import com.rex.commodis.model.events.TeamEvent;
import com.rex.commodis.repository.RexUserRepository;
import com.rex.commodis.repository.events.CorporateEventRepository;
import com.rex.commodis.repository.events.TeamEventRepository;
import com.rex.commodis.repository.invoices.*;
import com.rex.commodis.repository.invoices.InvoiceRepository;
import com.rex.commodis.service.PlatformConfigurationService;
import com.rex.commodis.service.RexUserService;
import com.rex.commodis.service.TeamEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rex.commodis.dto.invoiceDtos.InvoiceDetailsResponseDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.exceptions.ConflictException;
import com.rex.commodis.exceptions.NotFoundException;
import com.rex.commodis.model.enums.InvoiceSortBy;
import com.rex.commodis.model.enums.InvoiceStatusEnum;
import com.rex.commodis.model.enums.InvoiceStatusRequestEnum;

@Service
@RequiredArgsConstructor
public class InvoicesServiceImpl implements InvoicesService {

    private final InvoiceRepository invoiceRepository;
//    private final InvoiceMapper invoiceMapper;
//    private final InvoicePositionRepository invoicePositionRepository;
//    private final InvoiceHistoryService invoiceHistoryService;
////    private final InvoicePositionBeneficiaryRepository invoicePositionBeneficiaryRepository;
    private final InvoiceResponsibilityService invoiceResponsibilityService;
//    private final InvoiceGetListProjectionMapper invoiceProjectionMapper;
//    private final RexUserRepository rexUserRepository;
//    private final RexUserService rexUserService;
//    private final TeamEventService teamEventService;
//    private final GetDelegatedInvoiceMapper getDelegatedInvoiceMapper;
//    private final AssignedBeneficiaryMapper assignedBeneficiaryMapper;
//    private final PlatformConfigurationService platformConfigurationService;
//    private final InvoiceDetailsMapper invoiceDetailsMapper;
//    private final InvoiceToCorporateEventRepository invoiceToCorporateEventRepository;
    private final InvoiceToTeamEventRepository invoiceToTeamEventRepository;
    private final InvoiceStatusService invoiceStatusService;
    private final TeamEventRepository teamEventRepository;
    private final CorporateEventRepository corporateEventRepository;
    private final InvoiceToCorporateEventRepository invoiceToCorporateEventRepository;
    private final InvoiceToBeneficiaryRepository invoiceToBeneficiaryRepository;
    private final InvoiceToGuestRepository invoiceToGuestRepository;


    @Override
    @Transactional
    public InvoiceSaveDraftResponseDto processSaveDraft(Long rexUserId,
                                                        InvoiceSaveDraftRequestDto invoiceSaveDraftRequestDto) {

        Long invoiceId = invoiceSaveDraftRequestDto.getInvoiceId();

        InvoiceEventDataDto eventData = invoiceSaveDraftRequestDto.getEventData();

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new NotFoundException("Invoice not found: " + invoiceId));

        invoiceResponsibilityService.validateUserResponsibility(invoiceId, rexUserId);

        validateNoDuplicates(invoiceSaveDraftRequestDto.getBeneficiaryIds(), "beneficiary");
        validateNoDuplicates(invoiceSaveDraftRequestDto.getGuestIds(), "guest");
        validateEventData(eventData);


        if (invoiceSaveDraftRequestDto.getNote() != null) {
            invoice.setNote(invoiceSaveDraftRequestDto.getNote());
        }

        if (invoiceSaveDraftRequestDto.getTaxReasonId() != null) {
            invoice.setTaxReasonClientId(invoiceSaveDraftRequestDto.getTaxReasonId());
        }


        invoiceRepository.save(invoice);

        if (eventData != null
                && eventData.getEventId() != null
                && eventData.getEventId() != 0L
                && eventData.getEventSource() != null) {
            processEventData(invoiceId, eventData, invoiceSaveDraftRequestDto.getInheritanceUserSet());
        }

        syncInvoiceToBeneficiaries(invoiceId, invoiceSaveDraftRequestDto.getBeneficiaryIds());

        syncInvoiceToGuests(invoiceId, invoiceSaveDraftRequestDto.getGuestIds());

        invoiceStatusService.createOrUpdateDraftStatus(invoiceId, rexUserId);

        return buildResponse(invoiceId);
    }


//  Duplicate Validation

    private void validateNoDuplicates(List<Long> ids, String label) {
        if (ids == null || ids.isEmpty()) return;

        Set<Long> seen = new HashSet<>();
        List<Long> duplicates = ids.stream()
                .filter(id -> !seen.add(id))
                .distinct()
                .toList();

        if (!duplicates.isEmpty()) {
            throw new BadRequestException(
                    "Duplicate " + label + " IDs found: " + duplicates);
        }
    }


    private void validateEventData(
            InvoiceEventDataDto eventData) {

        if (eventData == null
                || eventData.getEventId() == null
                || eventData.getEventId() == 0L
                || eventData.getEventSource() == null) {
            return;
        }

        Long eventId = eventData.getEventId();
        String eventSource = eventData.getEventSource();

        switch (eventSource) {

            case "TEAM_EVENT" -> teamEventRepository
                    .findById(eventId)
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "Team event not found: "
                                            + eventId));

            case "CORPORATE_EVENT" -> corporateEventRepository
                    .findById(eventId)
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "Corporate event not found: "
                                            + eventId));

            default -> throw new BadRequestException(
                    "Unknown event source: "
                            + eventSource);
        }
    }


// Event Handling

    private void processEventData(Long invoiceId,
                                  InvoiceEventDataDto eventData,
                                  Boolean inheritanceUserSet) {
        Long eventId = eventData.getEventId();
        String eventSource = eventData.getEventSource();

        switch (eventSource) {

            case "TEAM_EVENT" -> {

                TeamEvent teamEvent = teamEventRepository.findById(eventId)
                        .get();

                InvoiceToTeamEvent mapping = invoiceToTeamEventRepository
                        .findByInvoiceId(invoiceId)
                        .orElseGet(() -> {
                            InvoiceToTeamEvent m = new InvoiceToTeamEvent();
                            m.setInvoiceId(invoiceId);
                            return m;
                        });

                mapping.setTeamEvent(teamEvent);
                invoiceToTeamEventRepository.save(mapping);
            }

            case "CORPORATE_EVENT" -> {
                CorporateEvent corporateEvent = corporateEventRepository.findById(eventId)
                        .get();

                InvoiceToCorporateEvent mapping = invoiceToCorporateEventRepository
                        .findByInvoiceId(invoiceId)
                        .orElseGet(() -> {
                            InvoiceToCorporateEvent m = new InvoiceToCorporateEvent();
                            m.setInvoiceId(invoiceId);
                            return m;
                        });

                mapping.setCorporateEvent(corporateEvent);
                invoiceToCorporateEventRepository.save(mapping);

                if (inheritanceUserSet != null) {
                    corporateEvent.setInheritanceUserSet(inheritanceUserSet);
                    corporateEventRepository.save(corporateEvent);
                }
            }
        }
    }


// Sync invoice_to_beneficiary

    private void syncInvoiceToBeneficiaries(Long invoiceId, List<Long> requestIds) {

        if (requestIds == null || requestIds.isEmpty()) {
            invoiceToBeneficiaryRepository.deleteAllByInvoiceId(invoiceId);
            return;
        }

        Set<Long> requestSet = new HashSet<>(requestIds);


        Set<Long> existingIds = new HashSet<>(
                invoiceToBeneficiaryRepository.findBeneficiaryIdsByInvoiceId(invoiceId));

        invoiceToBeneficiaryRepository
                .deleteByInvoiceIdAndBeneficiaryIdNotIn(invoiceId, requestIds);


        for (Long beneficiaryId : requestSet) {
            if (!existingIds.contains(beneficiaryId)) {
                InvoiceToBeneficiary record = InvoiceToBeneficiary.builder()
                        .invoiceId(invoiceId)
                        .beneficiaryId(beneficiaryId)
                        .build();
                invoiceToBeneficiaryRepository.save(record);
            }

        }
    }


// Sync invoice_to_guest

    private void syncInvoiceToGuests(Long invoiceId, List<Long> requestIds) {


        if (requestIds == null || requestIds.isEmpty()) {
            invoiceToGuestRepository.deleteAllByInvoiceId(invoiceId);
            return;
        }

        Set<Long> requestSet = new HashSet<>(requestIds);

        Set<Long> existingIds = new HashSet<>(
                invoiceToGuestRepository.findGuestIdsByInvoiceId(invoiceId));

        invoiceToGuestRepository
                .deleteByInvoiceIdAndGuestIdNotIn(invoiceId, requestIds);


        for (Long guestId : requestSet) {
            if (!existingIds.contains(guestId)) {
                InvoiceToGuest record = InvoiceToGuest.builder()
                        .invoiceId(invoiceId)
                        .guestId(guestId)
                        .build();
                invoiceToGuestRepository.save(record);
            }

        }
    }


// Build Response

    private InvoiceSaveDraftResponseDto buildResponse(Long invoiceId) {

        return InvoiceSaveDraftResponseDto.builder()
                .invoiceId(invoiceId)
                .build();
    }

//
//
//    @Override
//    @Transactional
//    public InvoiceGetListResponseDto getInvoices(
//            Long rexUserId,
//            String status,
//            String vendor,
//            LocalDate fromDate,
//            LocalDate toDate,
//            int pageNo,
//            int pageSize,
//            InvoiceSortBy sortBy,
//            String sortOrder
//    ) {
//
//        //Determine Sort By Field
//    	String resolvedSortBy = (sortBy != null)
//    	        ? sortBy.getDbField()
//    	        : InvoiceSortBy.INVOICE_DATE.getDbField();
//
//        //Sort Direction Resolution
//        Sort.Direction resolvedDirection = resolveSortDirection(sortOrder);
//
//        //Pagination Params
//        Pageable pageable = PageRequest.of(
//                pageNo - 1,
//                pageSize,
//                Sort.by(resolvedDirection, resolvedSortBy)
//        );
//
//        //Normalize status
//        String resolvedStatus = (status != null && !status.isBlank())
//                ? status.toUpperCase().trim()
//                : null;
//
//        //Get count of all invoices independent of filters
//        InvoiceStatusCountDto invoiceStatusCountDto = getGlobalInvoiceCounts(rexUserId);
//
//        // ===== EXISTING LOGIC (UNCHANGED) =====
//        Page<InvoiceGetListProjectionDto> page;
//
//        if (fromDate != null && toDate != null) {
//            page = invoiceRepository.findInvoicesForUserWithFiltersAndDates(
//                    rexUserId, resolvedStatus, vendor, fromDate, toDate, pageable
//            );
//
//        } else {
//            page = invoiceRepository.findInvoicesForUserWithFiltersNoDates(
//                    rexUserId, resolvedStatus, vendor, pageable
//            );
//        }
//
//        InvoiceGetListMetaDataDto meta = InvoiceGetListMetaDataDto.builder()
//                .rexUserId(rexUserId)
//                .from(fromDate)
//                .to(toDate)
//                .status(resolvedStatus)
//                .page(pageNo)
//                .pageSize(pageSize)
//                .sortBy(resolvedSortBy)
//                .direction(String.valueOf(resolvedDirection))
//                .build();
//
//        return InvoiceGetListResponseDto.builder()
//                .meta(meta)
//                .data(invoiceProjectionMapper.toDtoList(page.getContent()))
//                .statusCounts(invoiceStatusCountDto)
//                .build();
//    }
//
//
//
//
//    @Override
//    @Transactional
//    public void updateInvoiceStatus(Long invoiceId, InvoiceStatusRequestEnum status, Long rexUserId) {
//
//        invoiceResponsibilityService.validateUserResponsibility(invoiceId, rexUserId);
//
//        InvoiceStatusEnum statusEnum = InvoiceStatusEnum.valueOf(status.name());
//
//        invoiceHistoryService.validateDuplicateStatus(invoiceId, statusEnum);
//
//        invoiceHistoryService.createStatus(invoiceId, statusEnum, rexUserId);
//    }
//
//    @Override
//    @Transactional
//    public InvoiceDelegateDetailsResponseDto getDelegatedInvoices(
//            Long rexUserId,
//            LocalDate from,
//            LocalDate to,
//            int page,
//            int pageSize
//    ) {
//
//        Pageable pageable = PageRequest.of(page - 1, pageSize);
//
//        List<InvoiceStatusEnum> statuses = List.of(
//                InvoiceStatusEnum.DRAFT,
//                InvoiceStatusEnum.PENDING
//        );
//
//        Page<InvoiceDelegateDetailsProjection> results;
//        if (from != null && to != null) {
//
//            results = invoiceRepository.findDelegatedInvoicesByDateRange(
//                    rexUserId,
//                    from,
//                    to,
//                    statuses,
//                    pageable
//            );
//
//        } else {
//
//            results = invoiceRepository.findDelegatedInvoices(
//                    rexUserId,
//                    statuses,
//                    pageable
//            );
//        }
//
//        //Map using MapStruct
//        List<InvoiceDelegateDetailsDto> invoices =
//                getDelegatedInvoiceMapper.toDtoList(results.getContent());
//
//        //Get Username of delegated user
//        for (InvoiceDelegateDetailsDto dto : invoices) {
//
//            String delegateUserName =
//                    rexUserService.getUserNameById(dto.getDelegatedUserId());
//
//            dto.setDelegatedTo(delegateUserName);
//        }
//
//        // ✅ Step 4: Wrap response
//        return InvoiceDelegateDetailsResponseDto.builder()
//                .meta(
//                        InvoiceDelegateDetailsMetaDto.builder()
//                                .rexUserId(rexUserId)
//                                .from(from)
//                                .to(to)
//                                .page(page)
//                                .pageSize(pageSize)
//                                .build()
//                )
//                .data(invoices)
//                .build();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public InvoiceDetailsResponseDto getInvoiceDetails(Long invoiceId, Long rexUserId) {
//
//        invoiceResponsibilityService.validateUserResponsibility(invoiceId, rexUserId);
//
//        // Get main data (projection)
//        InvoiceDetailsResponseDto dto = invoiceRepository.getInvoiceDetails(invoiceId)
//                .orElseThrow(() -> new NotFoundException("Invoice not found"));
//
//        // Fetch positions separately
//        List<InvoicePosition> positions =
//                invoicePositionRepository.findByInvoiceId(invoiceId);
//
//        // Map positions using mapper
//        dto.setInvoicePositions(
//                invoiceDetailsMapper.toPositionResponseDtoList(positions)
//        );
//
//        dto.setEvent(buildEventDto(invoiceId));
//
//        return dto;
//    }
//
//    @Override
//    @Transactional
//    public InvoiceDelegateResponseDto delegateInvoice(Long invoiceId,
//                                                      InvoiceDelegateRequestDto requestDto,
//                                                      Long rexUserId) {
//
//        // 1. Validate invoice exists
//        if (!invoiceRepository.existsById(invoiceId)) {
//            throw new NotFoundException("Invoice not found with id: " + invoiceId);
//        }
//
//        // 2. Validate delegate user exists
//        if (!rexUserRepository.existsById(requestDto.getDelegateId())) {
//            throw new NotFoundException("Delegate user not found with id: " + requestDto.getDelegateId());
//        }
//
//        // 3. Prevent self-delegation
//        if (rexUserId.equals(requestDto.getDelegateId())) {
//            throw new ConflictException("You cannot delegate to yourself");
//        }
//
//        // 4. Validate current user responsibility
//        invoiceResponsibilityService.validateUserResponsibility(invoiceId, rexUserId);
//
//        // 5. Delegate
//        return invoiceResponsibilityService.delegateInvoice(invoiceId, requestDto, rexUserId);
//    }
//
//    @Override
//    @Transactional
//    public List<AssignedBeneficiaryDto> getAssignedBeneficiaries(Long invoiceId, Long rexUserId) {
//
//    	invoiceResponsibilityService.validateUserResponsibility(invoiceId, rexUserId);
//
//        List<Beneficiary> beneficiaries =
//                invoicePositionRepository.findBeneficiariesByInvoiceId(invoiceId);
//
//        return assignedBeneficiaryMapper.toDtoList(beneficiaries);
//    }
//
//
//    //Helper Method
//    public InvoiceStatusCountDto toInvoiceStatusCountDto(
//            List<InvoiceStatusCountProjection> invoiceStatusCountProjections,
//            long delegatedInvoiceCount
//    ) {
//
//        EnumMap<InvoiceStatusRequestEnum, Long> counts = new EnumMap<>(InvoiceStatusRequestEnum.class);
//
//        //Set all statuses to 0
//        for (InvoiceStatusRequestEnum status : InvoiceStatusRequestEnum.values()) {
//            counts.put(status, 0L);
//        }
//
//        //Map values from projection to dto
//        for (InvoiceStatusCountProjection invoiceStatusCountProjection : invoiceStatusCountProjections) {
//            InvoiceStatusRequestEnum statusEnum = InvoiceStatusRequestEnum.valueOf(invoiceStatusCountProjection.getStatus());
//            counts.put(statusEnum, invoiceStatusCountProjection.getCount());
//        }
//
//        //Build DTO
//        long total = counts.values()
//                .stream()
//                .mapToLong(Long::longValue)
//                .sum();
//
//        return InvoiceStatusCountDto.builder()
//                .total(total)
//                .byStatus(counts)
//                .delegatedInvoiceCount(delegatedInvoiceCount)
//                .build();
//    }
//
//
//    @Override
//    @Transactional(readOnly = true)
//    public InvoicePdfLinkResponseDto getInvoicePdfLink(Long invoiceId, Long rexUserId, Long platformId) {
//
//        // 1. Fetch invoice
//        Invoice invoice = invoiceRepository.findById(invoiceId)
//        		.orElseThrow(() -> new NotFoundException("Invoice not found"));
//
//        // 2. Validate responsibility
//        invoiceResponsibilityService.validateUserResponsibility(invoiceId, rexUserId);
//
//
//        PlatformConfiguration config = null;
//
//        // 4. Validate storage
//        if (config.getInvoiceStorage() == null ||
//                !"remote".equalsIgnoreCase(config.getInvoiceStorage())) {
//
//            throw new NotFoundException("Invoice PDF link not available");
//        }
//
//        // 5. Get values
//        String baseLink = config.getInvoiceStorageLink();
//        String documentNumber = invoice.getDocumentNumber();
//
//        if (baseLink == null || documentNumber == null) {
//            throw new NotFoundException("Invoice PDF link not available");
//        }
//
//        // 6. Construct final link
//        String finalLink = baseLink.endsWith("&INVOICEID=")
//                ? baseLink + documentNumber
//                : baseLink + "&INVOICEID=" + documentNumber;
//
//        // 7. Success response
//        return InvoicePdfLinkResponseDto.builder()
//                .invoicePdfLink(finalLink)
//                .build();
//    }
//
//    //Helper Methods
//    public InvoiceStatusCountDto toInvoiceStatusCountDto(
//            List<InvoiceStatusCountProjection> invoiceStatusCountProjections,
//            Long delegatedInvoiceCount
//    ) {
//
//        EnumMap<InvoiceStatusRequestEnum, Long> counts = new EnumMap<>(InvoiceStatusRequestEnum.class);
//
//        //Set all statuses to 0
//        for (InvoiceStatusRequestEnum status : InvoiceStatusRequestEnum.values()) {
//            counts.put(status, 0L);
//        }
//
//        //Map values from projection to dto
//        for (InvoiceStatusCountProjection invoiceStatusCountProjection : invoiceStatusCountProjections) {
//            InvoiceStatusRequestEnum statusEnum = InvoiceStatusRequestEnum.valueOf(invoiceStatusCountProjection.getStatus());
//            counts.put(statusEnum, invoiceStatusCountProjection.getCount());
//        }
//
//        //Build DTO
//        long total = counts.values()
//                .stream()
//                .mapToLong(Long::longValue)
//                .sum();
//
//        return InvoiceStatusCountDto.builder()
//                .total(total)
//                .byStatus(counts)
//                .delegatedInvoiceCount(delegatedInvoiceCount)
//                .build();
//    }
//
//    private Sort.Direction resolveSortDirection(String sortOrder){
//        if(sortOrder == null || sortOrder.isBlank())
//            return Sort.Direction.DESC;
//
//        if(!"ASC".equals(sortOrder) && !"DESC".equals(sortOrder)){
//            throw new BadRequestException(
//                    "Invalid sort_order: '" + sortOrder + "'. " +
//                            "Only 'ASC' or 'DESC' (uppercase) are accepted."
//            );
//        }
//
//        return Sort.Direction.valueOf(sortOrder);
//    }
//
//    private InvoiceStatusCountDto getGlobalInvoiceCounts(Long rexUserId){
//
//        List<InvoiceStatusEnum> statuses = List.of(
//                InvoiceStatusEnum.DRAFT,
//                InvoiceStatusEnum.PENDING
//        );
//
//        List<InvoiceStatusCountProjection> invoiceStatusCountProjections =
//                invoiceRepository.getStatusCounts(rexUserId);
//
//        Long delegatedInvoiceCount =
//                invoiceRepository.countDelegatedInvoices(rexUserId, statuses);
//
//        return toInvoiceStatusCountDto(
//                invoiceStatusCountProjections,
//                delegatedInvoiceCount
//        );
//    }
//
//    private InvoiceEventDto buildEventDto(Long invoiceId) {
//
//        Optional<InvoiceToCorporateEvent> invoiceToCorporateEvent =
//                invoiceToCorporateEventRepository.findByInvoiceId(invoiceId);
//
//        Optional<InvoiceToTeamEvent> invoiceToTeamEvent =
//                invoiceToTeamEventRepository.findByInvoiceId(invoiceId);
//
//
//        if (invoiceToCorporateEvent.isPresent() && invoiceToTeamEvent.isPresent()) {
//            throw new IllegalStateException("Invalid state: both mappings exist");
//        }
//
//        // CORPORATE
//        if (invoiceToCorporateEvent.isPresent()) {
//            CorporateEvent corporateEvent = invoiceToCorporateEvent.get().getCorporateEvent();
//
//            return InvoiceEventDto.builder()
//                    .eventSource("CORPORATE_EVENT")
//                    .eventId(corporateEvent.getId())
//                    .eventTitle(corporateEvent.getEventTitle())
//                    .inheritanceMode(corporateEvent.getInheritanceMode())
//                    .build();
//        }
//
//        // TEAM
//        if (invoiceToTeamEvent.isPresent()) {
//            TeamEvent teamEvent = invoiceToTeamEvent.get().getTeamEvent();
//
//            return InvoiceEventDto.builder()
//                    .eventSource("TEAM_EVENT")
//                    .eventId(teamEvent.getId())
//                    .eventTitle(teamEvent.getEventTitle())
//                    .inheritanceMode(null)
//                    .build();
//        }
//
//        return null;
//    }
//
//    @Transactional
//    public void updateProjectNumberId(Long invoiceId, Long projectNumberId){
//        invoiceRepository.updateProjectNumberId(invoiceId, projectNumberId);
//    }

    @Override
    public boolean existsByProjectNumber(Long projectNumberId) {
        return invoiceRepository.existsByProjectNumberId(projectNumberId);
    }

}


