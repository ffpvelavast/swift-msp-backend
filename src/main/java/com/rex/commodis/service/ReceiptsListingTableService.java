/** Service interface defining ReceiptsListingTable operations; implemented by ReceiptsListingTableServiceImpl. */
package com.rex.commodis.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.rex.commodis.dto.ReceiptGetListApproverResponseDto;
import com.rex.commodis.dto.ReceiptGetListSubmitterResponseDto;
import com.rex.commodis.model.enums.ReceiptSortApproverBy;
import com.rex.commodis.model.enums.ReceiptSortSubmittedBy;
import com.rex.commodis.model.enums.ReceiptStatusEnum;

public interface ReceiptsListingTableService {

    ReceiptGetListSubmitterResponseDto getSubmitterReceipts(
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
    );

    ReceiptGetListApproverResponseDto getApproverReceipts(
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
    );
}