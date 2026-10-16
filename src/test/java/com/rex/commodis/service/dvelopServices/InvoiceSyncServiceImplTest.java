package com.rex.commodis.service.dvelopServices;

import com.rex.commodis.dto.dvelopDtos.InvoiceTransferDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferHrefDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferLineItemDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferLinksDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferUserDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransfersResponseDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceVoucherDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceWorkflowDto;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.enums.InvoiceStatusEnum;
import com.rex.commodis.service.invoiceServices.InvoiceHistoryService;
import com.rex.commodis.service.invoiceServices.InvoicePositionService;
import com.rex.commodis.service.invoiceServices.InvoiceResponsibilityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceSyncServiceImplTest {

    @Mock private DvelopInvoiceClient dvelopInvoiceClient;
    @Mock private InvoiceVerifierRexUserLookupService invoiceVerifierRexUserLookupService;
    @Mock private InvoiceAssignmentMailService invoiceAssignmentMailService;
    @Mock private InvoiceDataPersistenceService invoiceDataPersistenceService;
    @Mock private InvoicePositionService invoicePositionService;
    @Mock private InvoiceHistoryService invoiceHistoryService;
    @Mock private InvoiceResponsibilityService invoiceResponsibilityService;

    @InjectMocks
    private InvoiceSyncServiceImpl service;

    private static final Long INVOICE_ID = 55L;
    private static final Long REX_USER_ID = 99L;
    private static final String DOCUMENT_LINK = "https://dms.example/document";
    private static final String CALLBACK_URL = "https://dms.example/callback";

    @Test
    void syncInvoices_withValidTransfer_acknowledgesPersistsAssignsResponsibilityAndSendsMail() {
        InvoiceTransferDto transfer = transfer("DOC-1", "regular custom10");
        InvoiceVoucherDto voucher = transfer.getWorkflow().getVoucher();
        RexUser rexUser = new RexUser();
        rexUser.setId(REX_USER_ID);
        rexUser.setEmail("verifier@example.com");
        rexUser.setFullName("Jane Doe");
        InvoiceTransfersResponseDto response = new InvoiceTransfersResponseDto();
        response.setTransfers(List.of(transfer));

        when(dvelopInvoiceClient.fetchTransfers()).thenReturn(response);
        when(invoiceDataPersistenceService.persistTransfer(voucher, DOCUMENT_LINK)).thenReturn(INVOICE_ID);
        when(invoicePositionService.findDistinctProjectNumbers(INVOICE_ID))
                .thenReturn(Set.of("100/200"));
        when(invoiceVerifierRexUserLookupService.resolveRexUserVerifier(voucher, INVOICE_ID))
                .thenReturn(rexUser);

        service.syncInvoices();

        verify(dvelopInvoiceClient).reportSuccess(CALLBACK_URL);
        verify(invoiceDataPersistenceService).persistTransfer(voucher, DOCUMENT_LINK);
        verify(invoicePositionService).findDistinctProjectNumbers(INVOICE_ID);
        verify(invoiceVerifierRexUserLookupService).resolveRexUserVerifier(voucher, INVOICE_ID);
        verify(invoiceHistoryService).createStatus(INVOICE_ID, InvoiceStatusEnum.PENDING, "Jane Doe");
        verify(invoiceResponsibilityService).createResponsibility(eq(INVOICE_ID), eq(REX_USER_ID), any(OffsetDateTime.class));
        verify(invoiceAssignmentMailService)
                .sendAssignmentMail("verifier@example.com", "Jane Doe", Set.of("100/200"), DOCUMENT_LINK);
    }

    @Test
    void syncInvoices_whenTransferIsMarkedOutOfScope_acknowledgesButSkipsProcessing() {
        InvoiceTransferDto transfer = transfer("DOC-1", "0 | do not process");
        InvoiceTransfersResponseDto response = new InvoiceTransfersResponseDto();
        response.setTransfers(List.of(transfer));
        when(dvelopInvoiceClient.fetchTransfers()).thenReturn(response);

        service.syncInvoices();

        verify(dvelopInvoiceClient).reportSuccess(CALLBACK_URL);
        verifyNoInteractions(invoiceDataPersistenceService, invoicePositionService,
                invoiceVerifierRexUserLookupService, invoiceHistoryService,
                invoiceResponsibilityService, invoiceAssignmentMailService);
    }

    @Test
    void syncInvoices_whenPersistenceReturnsNull_doesNotAssignInvoice() {
        InvoiceTransferDto transfer = transfer("DOC-1", null);
        InvoiceVoucherDto voucher = transfer.getWorkflow().getVoucher();
        InvoiceTransfersResponseDto response = new InvoiceTransfersResponseDto();
        response.setTransfers(List.of(transfer));

        when(dvelopInvoiceClient.fetchTransfers()).thenReturn(response);
        when(invoiceDataPersistenceService.persistTransfer(voucher, DOCUMENT_LINK)).thenReturn(null);

        service.syncInvoices();

        verify(dvelopInvoiceClient).reportSuccess(CALLBACK_URL);
        verify(invoiceDataPersistenceService).persistTransfer(voucher, DOCUMENT_LINK);
        verifyNoInteractions(invoicePositionService, invoiceVerifierRexUserLookupService,
                invoiceHistoryService, invoiceResponsibilityService, invoiceAssignmentMailService);
    }

    @Test
    void syncInvoices_whenRexUserCannotBeResolved_doesNotCreateStatusOrResponsibility() {
        InvoiceTransferDto transfer = transfer("DOC-1", null);
        InvoiceVoucherDto voucher = transfer.getWorkflow().getVoucher();
        InvoiceTransfersResponseDto response = new InvoiceTransfersResponseDto();
        response.setTransfers(List.of(transfer));

        when(dvelopInvoiceClient.fetchTransfers()).thenReturn(response);
        when(invoiceDataPersistenceService.persistTransfer(voucher, DOCUMENT_LINK)).thenReturn(INVOICE_ID);
        when(invoicePositionService.findDistinctProjectNumbers(INVOICE_ID)).thenReturn(Set.of("100/200"));
        when(invoiceVerifierRexUserLookupService.resolveRexUserVerifier(voucher, INVOICE_ID))
                .thenReturn(null);

        service.syncInvoices();

        verify(invoicePositionService).findDistinctProjectNumbers(INVOICE_ID);
        verify(invoiceVerifierRexUserLookupService).resolveRexUserVerifier(voucher, INVOICE_ID);
        verify(invoiceHistoryService, never()).createStatus(any(), any(), any());
        verify(invoiceResponsibilityService, never()).createResponsibility(any(), any(), any());
        verifyNoInteractions(invoiceAssignmentMailService);
    }

    @Test
    void syncInvoices_whenNoTransfers_doesNothingBeyondFetch() {
        InvoiceTransfersResponseDto response = new InvoiceTransfersResponseDto();
        response.setTransfers(List.of());
        when(dvelopInvoiceClient.fetchTransfers()).thenReturn(response);

        service.syncInvoices();

        verify(dvelopInvoiceClient).fetchTransfers();
        verify(dvelopInvoiceClient, never()).reportSuccess(any());
        verifyNoInteractions(invoiceDataPersistenceService, invoicePositionService,
                invoiceVerifierRexUserLookupService, invoiceHistoryService,
                invoiceResponsibilityService, invoiceAssignmentMailService);
    }

    private InvoiceTransferDto transfer(String documentId, String custom10) {
        InvoiceTransferHrefDto callback = new InvoiceTransferHrefDto();
        callback.setHref(" " + CALLBACK_URL + " ");
        InvoiceTransferHrefDto dmsObject = new InvoiceTransferHrefDto();
        dmsObject.setHref(DOCUMENT_LINK);
        InvoiceTransferLinksDto links = new InvoiceTransferLinksDto();
        links.setReportResultsAsync(callback);
        links.setDmsObject(dmsObject);

        InvoiceTransferUserDto verifier = new InvoiceTransferUserDto();
        verifier.setName("verifier@example.com");
        InvoiceTransferLineItemDto lineItem = new InvoiceTransferLineItemDto();
        lineItem.setVerifiedBy(verifier);

        InvoiceVoucherDto voucher = new InvoiceVoucherDto();
        voucher.setDocId(documentId);
        voucher.setCustom10(custom10);
        voucher.setLineItems(Map.of("1", lineItem));
        InvoiceWorkflowDto workflow = new InvoiceWorkflowDto();
        workflow.setVoucher(voucher);

        InvoiceTransferDto transfer = new InvoiceTransferDto();
        transfer.setLinks(links);
        transfer.setWorkflow(workflow);
        return transfer;
    }
}
