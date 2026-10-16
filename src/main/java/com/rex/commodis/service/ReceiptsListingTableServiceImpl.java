/** Service implementation for ReceiptsListingTable business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.mapper.ReceiptGetListApproverProjectionMapper;
import com.rex.commodis.mapper.ReceiptGetListSubmitterProjectionMapper;
import com.rex.commodis.model.enums.ReceiptSortApproverBy;
import com.rex.commodis.model.enums.ReceiptSortSubmittedBy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rex.commodis.dto.*;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.repository.receipts.ReceiptRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReceiptsListingTableServiceImpl implements ReceiptsListingTableService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptGetListApproverProjectionMapper receiptApproverProjectionMapper;
    private final ReceiptGetListSubmitterProjectionMapper receiptSubmitterProjectionMapper;
    private final RexUserService rexUserService;

    @Override
    @Transactional(readOnly = true)
    public ReceiptGetListSubmitterResponseDto getSubmitterReceipts(
            Long rexUserId,
            ReceiptStatusEnum status,
            String receiptTitle,
            String submittedBy,
            LocalDate receiptFromDate,
            LocalDate receiptToDate,
            String category,
            BigDecimal receiptAmount,
            int pageNo,
            int pageSize,
            ReceiptSortSubmittedBy sortBy,
            String sortOrder
    ) {

        //Sort Direction Resolution
        Sort.Direction resolvedDirection = resolveSortDirection(sortOrder);

        //Determine Sort By Field
        String sortByValue = (sortBy != null)
                ? sortBy.name().toLowerCase()
                : "receipt_date";

        //Pagination Params
        Sort sort;
        if (sortBy == ReceiptSortSubmittedBy.STATUS) {
            sort = Sort.unsorted();
        } else {
            String field = (sortBy != null) ? sortBy.getField() : "receiptDate";
            sort = Sort.by(resolvedDirection, field);
        }

        Pageable pageable = PageRequest.of(
                pageNo - 1,
                pageSize
        );

        // Normalize the receiptStatus, receiptTitle, & expenseCategory
        String receiptStatus = (status != null) ? status.name() : null;
        String normalizedReceiptTitle = (receiptTitle != null && !receiptTitle.isBlank()) ? receiptTitle.trim() : null;
        String expenseCategory =(category != null && !category.isBlank()) ? category.trim() : null;

        String locale = rexUserService.getLocale(rexUserId);
        //TODO : Need to figure out how to differentiate between submitter and approver fullNames for submitterBy
        String normalizedSubmittedBy = (submittedBy != null && !submittedBy.isBlank()) ? submittedBy.trim() : null;

        //Find Receipts by filters
        Page<ReceiptGetListSubmitterProjectionDto> page =
                receiptRepository.findSubmitterReceiptsWithFilters(
                        rexUserId,
                        receiptStatus,
                        normalizedReceiptTitle,
                        normalizedSubmittedBy,
                        expenseCategory,
                        locale,
                        receiptAmount,
                        receiptFromDate,
                        receiptToDate,
                        sortByValue,
                        resolvedDirection.name(),
                        pageable
                );

        // Build Response
        ReceiptGetListSubmitterMetaDto receiptGetListSubmitterMetaDto =
                ReceiptGetListSubmitterMetaDto.builder()
                        .rexUserId(rexUserId)
                        .status(receiptStatus)
                        .receiptTitle(normalizedReceiptTitle)
                        .submittedBy(normalizedSubmittedBy)
                        .receiptFromDate(receiptFromDate)
                        .receiptToDate(receiptToDate)
                        .category(expenseCategory)
                        .receiptAmount(receiptAmount)
                        .page(pageNo)
                        .pageSize(pageSize)
                        .sortBy(sortByValue)
                        .direction(resolvedDirection.name())
                        .build();

        return ReceiptGetListSubmitterResponseDto.builder()
                .meta(receiptGetListSubmitterMetaDto)
                .data(receiptSubmitterProjectionMapper.toDtoList(page.getContent()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ReceiptGetListApproverResponseDto getApproverReceipts(
            Long rexUserId,
            ReceiptStatusEnum status,
            String receiptTitle,
            String submittedFor,
            LocalDate receiptFromDate,
            LocalDate receiptToDate,
            String category,
            BigDecimal receiptAmount,
            int pageNo,
            int pageSize,
            ReceiptSortApproverBy sortBy,
            String sortOrder
    ) {

        //Sort Direction Resolution
        Sort.Direction resolvedDirection = resolveSortDirection(sortOrder);

        //Determine Sort By Field
        String sortByValue = (sortBy != null)
                ? sortBy.name().toLowerCase()
                : "receipt_date";

        //Pagination Params
        Sort sort;
        if (sortBy == ReceiptSortApproverBy.STATUS) {
            sort = Sort.unsorted();
        } else {
            String field = (sortBy != null) ? sortBy.getField() : "receiptDate";
            sort = Sort.by(resolvedDirection, field);
        }

        Pageable pageable = PageRequest.of(
                pageNo - 1,
                pageSize
        );

        // Normalize the receiptStatus, receiptTitle, & expenseCategory
        String receiptStatus = (status != null) ? status.name() : null;
        String normalizedReceiptTitle = (receiptTitle != null && !receiptTitle.isBlank()) ? receiptTitle.trim() : null;
        String expenseCategory =(category != null && !category.isBlank()) ? category.trim() : null;
        String locale = rexUserService.getLocale(rexUserId);

        //TODO : Need to figure out how to differentiate between submitter and approver fullNames for submitterBy
        String normalizedSubmittedFor = (submittedFor != null && !submittedFor.isBlank()) ? submittedFor.trim() : null;

        //Find Receipts by filters
        Page<ReceiptGetListApproverProjectionDto> page =
                receiptRepository.findApproverReceiptsWithFilters(
                        rexUserId,
                        receiptStatus,
                        normalizedReceiptTitle,
                        normalizedSubmittedFor,
                        expenseCategory,
                        locale,
                        receiptAmount,
                        receiptFromDate,
                        receiptToDate,
                        sortByValue,
                        resolvedDirection.name(),
                        pageable
                );

        // Build Response
        ReceiptGetListApproverMetaDto receiptGetListApproverMetaDto =
                ReceiptGetListApproverMetaDto.builder()
                        .rexUserId(rexUserId)
                        .status(receiptStatus)
                        .receiptTitle(normalizedReceiptTitle)
                        .submittedFor(normalizedSubmittedFor)
                        .receiptFromDate(receiptFromDate)
                        .receiptToDate(receiptToDate)
                        .category(expenseCategory)
                        .receiptAmount(receiptAmount)
                        .page(pageNo)
                        .pageSize(pageSize)
                        .sortBy(sortByValue)
                        .direction(resolvedDirection.name())
                        .build();

        return ReceiptGetListApproverResponseDto.builder()
                .meta(receiptGetListApproverMetaDto)
                .data(receiptApproverProjectionMapper.toDtoList(page.getContent()))
                .build();
    }

    private Sort.Direction resolveSortDirection(String sortOrder){
        if(sortOrder == null || sortOrder.isBlank())
            return Sort.Direction.DESC;

        if(!"ASC".equals(sortOrder) && !"DESC".equals(sortOrder)){
            throw new BadRequestException(
                    "Invalid sort_order: '" + sortOrder + "'. " +
                            "Only 'ASC' or 'DESC' (uppercase) are accepted."
            );
        }

        return Sort.Direction.valueOf(sortOrder);
    }
}