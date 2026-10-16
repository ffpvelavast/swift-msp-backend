/** Service implementation for DashboardApproverReceiptList business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.dto.receipts.DashboardApproverReceiptListItemDto;
import com.rex.commodis.dto.receipts.DashboardApproverReceiptListProjection;
import com.rex.commodis.dto.receipts.DashboardApproverReceiptListResponseDto;
import com.rex.commodis.repository.receipts.DashboardApproverReceiptListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardApproverReceiptListServiceImpl implements DashboardApproverReceiptListService {

    private final DashboardApproverReceiptListRepository repository;
    private final RexUserService rexUserService;

    @Override
    public DashboardApproverReceiptListResponseDto getDashboardApproverReceiptList(
            Long rexUserId
    ) {
        String locale = rexUserService.getLocale(rexUserId);

        List<DashboardApproverReceiptListProjection> projections =
                repository.getDashboardApproverReceiptList(rexUserId, locale);

        List<DashboardApproverReceiptListItemDto> data = projections.stream()
                .map(projection -> DashboardApproverReceiptListItemDto.builder()
                        .receiptId(projection.getReceiptId())
                        .receiptTitle(projection.getReceiptTitle())
                        .amount(projection.getAmount())
                        .currency(projection.getCurrency())
                        .status(projection.getStatus())
                        .build())
                .toList();

        return DashboardApproverReceiptListResponseDto.builder()
                .data(data)
                .build();
    }
}