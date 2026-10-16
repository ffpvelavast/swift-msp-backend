/** Service interface defining Invoices operations; implemented by InvoicesServiceImpl. */
package com.rex.commodis.service.invoiceServices;

import java.time.LocalDate;
import java.util.List;

import com.rex.commodis.dto.*;
import com.rex.commodis.dto.invoiceDtos.*;
import com.rex.commodis.model.enums.InvoiceSortBy;
import com.rex.commodis.model.enums.InvoiceStatusRequestEnum;

public interface InvoicesService {

    InvoiceSaveDraftResponseDto processSaveDraft(Long rexUserId, InvoiceSaveDraftRequestDto request);

//    void updateInvoiceStatus(Long invoiceId, InvoiceStatusRequestEnum status, Long rexUserId);
//
//    InvoiceDetailsResponseDto getInvoiceDetails(Long invoiceId, Long rexUserId);
//
//    InvoiceDelegateDetailsResponseDto getDelegatedInvoices(
//            Long rexUserId, LocalDate from, LocalDate to, int page, int pageSize
//    );
//
//    InvoiceDelegateResponseDto delegateInvoice(Long invoiceId, InvoiceDelegateRequestDto request, Long rexUserId);
//
//    List<AssignedBeneficiaryDto> getAssignedBeneficiaries(Long invoiceId, Long rexUserId);
//
//    InvoicePdfLinkResponseDto getInvoicePdfLink(Long invoiceId, Long rexUserId, Long platformId);
//
//	InvoiceGetListResponseDto getInvoices(Long rexUserId, String status, String vendor, LocalDate fromDate,
//			LocalDate toDate, int pageNo, int pageSize, InvoiceSortBy sortBy, String sortOrder);
//
//    void updateProjectNumberId(Long invoiceId, Long projectNumberId);

    public boolean existsByProjectNumber(Long projectNumberId);

}
