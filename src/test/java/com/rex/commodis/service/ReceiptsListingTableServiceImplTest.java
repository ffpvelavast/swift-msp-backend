package com.rex.commodis.service;

import com.rex.commodis.dto.ReceiptGetListApproverDto;
import com.rex.commodis.dto.ReceiptGetListApproverProjectionDto;
import com.rex.commodis.dto.ReceiptGetListApproverResponseDto;
import com.rex.commodis.dto.ReceiptGetListSubmitterDto;
import com.rex.commodis.dto.ReceiptGetListSubmitterProjectionDto;
import com.rex.commodis.dto.ReceiptGetListSubmitterResponseDto;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.mapper.ReceiptGetListApproverProjectionMapper;
import com.rex.commodis.mapper.ReceiptGetListSubmitterProjectionMapper;
import com.rex.commodis.model.enums.ReceiptSortApproverBy;
import com.rex.commodis.model.enums.ReceiptSortSubmittedBy;
import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.repository.receipts.ReceiptRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptsListingTableServiceImplTest {

    @Mock private ReceiptRepository receiptRepository;
    @Mock private ReceiptGetListApproverProjectionMapper receiptApproverProjectionMapper;
    @Mock private ReceiptGetListSubmitterProjectionMapper receiptSubmitterProjectionMapper;
    @Mock private RexUserService rexUserService;

    @InjectMocks
    private ReceiptsListingTableServiceImpl receiptsListingTableService;

    private static final Long REX_USER_ID = 1L;
    private static final String LOCALE = "en_US";

    @Test
    void getSubmitterReceipts_withFilters_normalizesInputsDelegatesToRepositoryAndBuildsResponse() {
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        LocalDate toDate = LocalDate.of(2024, 1, 31);
        BigDecimal amount = new BigDecimal("125.50");
        ReceiptGetListSubmitterProjectionDto projection = mock(ReceiptGetListSubmitterProjectionDto.class);
        ReceiptGetListSubmitterDto dto = ReceiptGetListSubmitterDto.builder()
                .receiptId(10L)
                .receiptTitle("Client lunch")
                .build();
        List<ReceiptGetListSubmitterProjectionDto> projections = List.of(projection);
        List<ReceiptGetListSubmitterDto> mappedDtos = List.of(dto);
        Page<ReceiptGetListSubmitterProjectionDto> page = new PageImpl<>(projections);

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(receiptRepository.findSubmitterReceiptsWithFilters(
                eq(REX_USER_ID),
                eq(ReceiptStatusEnum.SUBMITTED.name()),
                eq("Client lunch"),
                eq("Jane Doe"),
                eq("Meals"),
                eq(LOCALE),
                eq(amount),
                eq(fromDate),
                eq(toDate),
                eq("receipt_amount"),
                eq("ASC"),
                org.mockito.ArgumentMatchers.any(Pageable.class)
        )).thenReturn(page);
        when(receiptSubmitterProjectionMapper.toDtoList(projections)).thenReturn(mappedDtos);

        ReceiptGetListSubmitterResponseDto response =
                receiptsListingTableService.getSubmitterReceipts(
                        REX_USER_ID,
                        ReceiptStatusEnum.SUBMITTED,
                        "  Client lunch  ",
                        "  Jane Doe  ",
                        fromDate,
                        toDate,
                        "  Meals  ",
                        amount,
                        2,
                        25,
                        ReceiptSortSubmittedBy.RECEIPT_AMOUNT,
                        "ASC"
                );

        assertThat(response.getData()).isEqualTo(mappedDtos);
        assertThat(response.getMeta().getRexUserId()).isEqualTo(REX_USER_ID);
        assertThat(response.getMeta().getStatus()).isEqualTo(ReceiptStatusEnum.SUBMITTED.name());
        assertThat(response.getMeta().getReceiptTitle()).isEqualTo("Client lunch");
        assertThat(response.getMeta().getSubmittedBy()).isEqualTo("Jane Doe");
        assertThat(response.getMeta().getReceiptFromDate()).isEqualTo(fromDate);
        assertThat(response.getMeta().getReceiptToDate()).isEqualTo(toDate);
        assertThat(response.getMeta().getCategory()).isEqualTo("Meals");
        assertThat(response.getMeta().getReceiptAmount()).isEqualTo(amount);
        assertThat(response.getMeta().getPage()).isEqualTo(2);
        assertThat(response.getMeta().getPageSize()).isEqualTo(25);
        assertThat(response.getMeta().getSortBy()).isEqualTo("receipt_amount");
        assertThat(response.getMeta().getDirection()).isEqualTo("ASC");

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(receiptRepository).findSubmitterReceiptsWithFilters(
                eq(REX_USER_ID),
                eq(ReceiptStatusEnum.SUBMITTED.name()),
                eq("Client lunch"),
                eq("Jane Doe"),
                eq("Meals"),
                eq(LOCALE),
                eq(amount),
                eq(fromDate),
                eq(toDate),
                eq("receipt_amount"),
                eq("ASC"),
                pageableCaptor.capture()
        );
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(1);
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(25);
        verify(receiptSubmitterProjectionMapper).toDtoList(projections);
    }

    @Test
    void getSubmitterReceipts_withBlankOptionalFiltersAndDefaultSort_usesNullFiltersAndDescendingReceiptDate() {
        Page<ReceiptGetListSubmitterProjectionDto> page = new PageImpl<>(List.of());

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(receiptRepository.findSubmitterReceiptsWithFilters(
                eq(REX_USER_ID),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(LOCALE),
                eq(null),
                eq(null),
                eq(null),
                eq("receipt_date"),
                eq("DESC"),
                org.mockito.ArgumentMatchers.any(Pageable.class)
        )).thenReturn(page);
        when(receiptSubmitterProjectionMapper.toDtoList(List.of())).thenReturn(List.of());

        ReceiptGetListSubmitterResponseDto response =
                receiptsListingTableService.getSubmitterReceipts(
                        REX_USER_ID,
                        null,
                        "   ",
                        "",
                        null,
                        null,
                        "   ",
                        null,
                        1,
                        10,
                        null,
                        null
                );

        assertThat(response.getData()).isEmpty();
        assertThat(response.getMeta().getStatus()).isNull();
        assertThat(response.getMeta().getReceiptTitle()).isNull();
        assertThat(response.getMeta().getSubmittedBy()).isNull();
        assertThat(response.getMeta().getCategory()).isNull();
        assertThat(response.getMeta().getReceiptAmount()).isNull();
        assertThat(response.getMeta().getPage()).isEqualTo(1);
        assertThat(response.getMeta().getPageSize()).isEqualTo(10);
        assertThat(response.getMeta().getSortBy()).isEqualTo("receipt_date");
        assertThat(response.getMeta().getDirection()).isEqualTo("DESC");

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(receiptRepository).findSubmitterReceiptsWithFilters(
                eq(REX_USER_ID),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(LOCALE),
                eq(null),
                eq(null),
                eq(null),
                eq("receipt_date"),
                eq("DESC"),
                pageableCaptor.capture()
        );
        assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(10);
    }

    @Test
    void getSubmitterReceipts_withInvalidSortOrder_throwsBadRequestException() {
        assertThatThrownBy(() ->
                receiptsListingTableService.getSubmitterReceipts(
                        REX_USER_ID,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        1,
                        10,
                        ReceiptSortSubmittedBy.RECEIPT_DATE,
                        "asc"
                ))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid sort_order: 'asc'");

        verifyNoInteractions(receiptRepository, receiptSubmitterProjectionMapper, rexUserService);
    }

    @Test
    void getApproverReceipts_withFilters_normalizesInputsDelegatesToRepositoryAndBuildsResponse() {
        LocalDate fromDate = LocalDate.of(2024, 2, 1);
        LocalDate toDate = LocalDate.of(2024, 2, 29);
        BigDecimal amount = new BigDecimal("250.75");
        ReceiptGetListApproverProjectionDto projection = mock(ReceiptGetListApproverProjectionDto.class);
        ReceiptGetListApproverDto dto = ReceiptGetListApproverDto.builder()
                .receiptId(20L)
                .receiptTitle("Hotel stay")
                .submittedFor("John Smith")
                .build();
        List<ReceiptGetListApproverProjectionDto> projections = List.of(projection);
        List<ReceiptGetListApproverDto> mappedDtos = List.of(dto);
        Page<ReceiptGetListApproverProjectionDto> page = new PageImpl<>(projections);

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(receiptRepository.findApproverReceiptsWithFilters(
                eq(REX_USER_ID),
                eq(ReceiptStatusEnum.APPROVED.name()),
                eq("Hotel stay"),
                eq("John Smith"),
                eq("Travel"),
                eq(LOCALE),
                eq(amount),
                eq(fromDate),
                eq(toDate),
                eq("submitted_for"),
                eq("DESC"),
                org.mockito.ArgumentMatchers.any(Pageable.class)
        )).thenReturn(page);
        when(receiptApproverProjectionMapper.toDtoList(projections)).thenReturn(mappedDtos);

        ReceiptGetListApproverResponseDto response =
                receiptsListingTableService.getApproverReceipts(
                        REX_USER_ID,
                        ReceiptStatusEnum.APPROVED,
                        "  Hotel stay  ",
                        "  John Smith  ",
                        fromDate,
                        toDate,
                        "  Travel  ",
                        amount,
                        3,
                        15,
                        ReceiptSortApproverBy.SUBMITTED_FOR,
                        "DESC"
                );

        assertThat(response.getData()).isEqualTo(mappedDtos);
        assertThat(response.getMeta().getRexUserId()).isEqualTo(REX_USER_ID);
        assertThat(response.getMeta().getStatus()).isEqualTo(ReceiptStatusEnum.APPROVED.name());
        assertThat(response.getMeta().getReceiptTitle()).isEqualTo("Hotel stay");
        assertThat(response.getMeta().getSubmittedFor()).isEqualTo("John Smith");
        assertThat(response.getMeta().getReceiptFromDate()).isEqualTo(fromDate);
        assertThat(response.getMeta().getReceiptToDate()).isEqualTo(toDate);
        assertThat(response.getMeta().getCategory()).isEqualTo("Travel");
        assertThat(response.getMeta().getReceiptAmount()).isEqualTo(amount);
        assertThat(response.getMeta().getPage()).isEqualTo(3);
        assertThat(response.getMeta().getPageSize()).isEqualTo(15);
        assertThat(response.getMeta().getSortBy()).isEqualTo("submitted_for");
        assertThat(response.getMeta().getDirection()).isEqualTo("DESC");

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(receiptRepository).findApproverReceiptsWithFilters(
                eq(REX_USER_ID),
                eq(ReceiptStatusEnum.APPROVED.name()),
                eq("Hotel stay"),
                eq("John Smith"),
                eq("Travel"),
                eq(LOCALE),
                eq(amount),
                eq(fromDate),
                eq(toDate),
                eq("submitted_for"),
                eq("DESC"),
                pageableCaptor.capture()
        );
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(2);
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(15);
        verify(receiptApproverProjectionMapper).toDtoList(projections);
    }

    @Test
    void getApproverReceipts_withBlankOptionalFiltersAndDefaultSort_usesNullFiltersAndDescendingReceiptDate() {
        Page<ReceiptGetListApproverProjectionDto> page = new PageImpl<>(List.of());

        when(rexUserService.getLocale(REX_USER_ID)).thenReturn(LOCALE);
        when(receiptRepository.findApproverReceiptsWithFilters(
                eq(REX_USER_ID),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(LOCALE),
                eq(null),
                eq(null),
                eq(null),
                eq("receipt_date"),
                eq("DESC"),
                org.mockito.ArgumentMatchers.any(Pageable.class)
        )).thenReturn(page);
        when(receiptApproverProjectionMapper.toDtoList(List.of())).thenReturn(List.of());

        ReceiptGetListApproverResponseDto response =
                receiptsListingTableService.getApproverReceipts(
                        REX_USER_ID,
                        null,
                        "   ",
                        "",
                        null,
                        null,
                        "   ",
                        null,
                        1,
                        10,
                        null,
                        null
                );

        assertThat(response.getData()).isEmpty();
        assertThat(response.getMeta().getStatus()).isNull();
        assertThat(response.getMeta().getReceiptTitle()).isNull();
        assertThat(response.getMeta().getSubmittedFor()).isNull();
        assertThat(response.getMeta().getCategory()).isNull();
        assertThat(response.getMeta().getReceiptAmount()).isNull();
        assertThat(response.getMeta().getPage()).isEqualTo(1);
        assertThat(response.getMeta().getPageSize()).isEqualTo(10);
        assertThat(response.getMeta().getSortBy()).isEqualTo("receipt_date");
        assertThat(response.getMeta().getDirection()).isEqualTo("DESC");

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(receiptRepository).findApproverReceiptsWithFilters(
                eq(REX_USER_ID),
                eq(null),
                eq(null),
                eq(null),
                eq(null),
                eq(LOCALE),
                eq(null),
                eq(null),
                eq(null),
                eq("receipt_date"),
                eq("DESC"),
                pageableCaptor.capture()
        );
        assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(10);
    }

    @Test
    void getApproverReceipts_withInvalidSortOrder_throwsBadRequestException() {
        assertThatThrownBy(() ->
                receiptsListingTableService.getApproverReceipts(
                        REX_USER_ID,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        1,
                        10,
                        ReceiptSortApproverBy.RECEIPT_DATE,
                        "asc"
                ))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid sort_order: 'asc'");

        verifyNoInteractions(receiptRepository, receiptApproverProjectionMapper, rexUserService);
    }
}
