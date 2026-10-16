package com.rex.commodis.service.dvelopServices;

import com.rex.commodis.dto.dvelopDtos.InvoiceTransferCompanyDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferCurrencyDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferDocumentTypeDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferLineItemDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferQuantityDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferTaxCodeDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferUserDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferVendorDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceVoucherDto;
import com.rex.commodis.model.invoices.Invoice;
import com.rex.commodis.model.invoices.InvoicePosition;
import com.rex.commodis.repository.invoices.InvoicePositionRepository;
import com.rex.commodis.repository.invoices.InvoiceRepository;
import com.rex.commodis.service.ProjectNumberService;
import com.rex.commodis.service.invoiceServices.InvoiceErrorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceDataPersistenceServiceTest {

    @Mock private InvoiceErrorService invoiceErrorService;
    @Mock private InvoiceRepository invoiceRepository;
    @Mock private InvoicePositionRepository invoicePositionRepository;
    @Mock private ProjectNumberService projectNumberService;
    @Mock private InvoiceAssignmentMailService invoiceAssignmentMailService;
    @Mock private InvoiceVerifierRexUserLookupService invoiceVerifierRexUserLookupService;

    @InjectMocks
    private InvoiceDataPersistenceService service;

    private static final String DOCUMENT_ID = "DOC-1";
    private static final String DOCUMENT_LINK = "https://dms.example/document";
    private static final String VERIFIER_EMAIL = "verifier@example.com";

    @Test
    void persistTransfer_withValidVoucher_savesInvoiceAndPositionsAndReturnsInvoiceId() {
        ReflectionTestUtils.setField(service, "sourceSystem", "DVELOP");
        InvoiceVoucherDto voucher = validVoucher();
        when(invoiceVerifierRexUserLookupService.getVerifierEmail(voucher)).thenReturn(VERIFIER_EMAIL);
        when(projectNumberService.resolveProjectNumberId(DOCUMENT_ID, "100/200/300")).thenReturn(77L);
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            invoice.setId(55L);
            return invoice;
        });

        Long result = service.persistTransfer(voucher, DOCUMENT_LINK);

        assertThat(result).isEqualTo(55L);
        ArgumentCaptor<Invoice> invoiceCaptor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceRepository).save(invoiceCaptor.capture());
        Invoice savedInvoice = invoiceCaptor.getValue();
        assertThat(savedInvoice.getDocumentNumber()).isEqualTo(DOCUMENT_ID);
        assertThat(savedInvoice.getDocumentName()).isEqualTo("Invoice");
        assertThat(savedInvoice.getDocumentType()).isEqualTo("Invoice");
        assertThat(savedInvoice.getDocumentLink()).isEqualTo(DOCUMENT_LINK);
        assertThat(savedInvoice.getDocumentDate()).isEqualTo(OffsetDateTime.of(2024, 1, 2, 0, 0, 0, 0, ZoneOffset.UTC).toLocalDate());
        assertThat(savedInvoice.getRecipient()).isEqualTo("Commodis GmbH");
        assertThat(savedInvoice.getVendor()).isEqualTo("Acme GmbH");
        assertThat(savedInvoice.getCurrency()).isEqualTo("EUR");
        assertThat(savedInvoice.getInvoiceTotalGross()).isEqualByComparingTo("119.00");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<InvoicePosition>> positionsCaptor = ArgumentCaptor.forClass(List.class);
        verify(invoicePositionRepository).saveAll(positionsCaptor.capture());
        assertThat(positionsCaptor.getValue()).hasSize(1);
        InvoicePosition savedPosition = positionsCaptor.getValue().get(0);
        assertThat(savedPosition.getInvoiceId()).isEqualTo(55L);
        assertThat(savedPosition.getExternalPositionNumber()).isEqualTo("POS-1");
        assertThat(savedPosition.getFactualApprover()).isEqualTo(VERIFIER_EMAIL);
        assertThat(savedPosition.getPositionText()).isEqualTo("Consulting");
        assertThat(savedPosition.getProjectNumber()).isEqualTo("100/200/300");
        assertThat(savedPosition.getProjectNumberId()).isEqualTo(77L);
        assertThat(savedPosition.getPositionVatRate()).isEqualTo(19);
        assertThat(savedPosition.getPositionQuantity()).isEqualTo(2);
    }

    @Test
    void persistTransfer_whenRequiredInvoiceFieldMissing_logsErrorAndDoesNotPersist() {
        InvoiceVoucherDto voucher = validVoucher();
        voucher.setDocumentType(null);
        when(invoiceVerifierRexUserLookupService.getVerifierEmail(voucher)).thenReturn(VERIFIER_EMAIL);

        Long result = service.persistTransfer(voucher, DOCUMENT_LINK);

        assertThat(result).isNull();
        verify(invoiceErrorService).logRequiredFieldError(
                "INVOICES",
                "document_type.name",
                DOCUMENT_ID,
                null,
                "Required field is missing",
                1
        );
        verify(invoiceAssignmentMailService).sendMissingRequiredFieldEmail("INVOICES", "document_type.name");
        verify(invoiceAssignmentMailService)
                .sendInvoiceAssignmentFailureMail(VERIFIER_EMAIL, "INVOICES", "document_type.name");
        verify(invoiceRepository, never()).save(any(Invoice.class));
        verifyNoInteractions(invoicePositionRepository);
    }

    @Test
    void persistTransfer_whenInvoiceSaveFails_logsPersistenceErrorAndReturnsNull() {
        ReflectionTestUtils.setField(service, "sourceSystem", "DVELOP");
        InvoiceVoucherDto voucher = validVoucher();
        when(invoiceVerifierRexUserLookupService.getVerifierEmail(voucher)).thenReturn(VERIFIER_EMAIL);
        when(projectNumberService.resolveProjectNumberId(DOCUMENT_ID, "100/200/300")).thenReturn(77L);
        when(invoiceRepository.save(any(Invoice.class))).thenThrow(new RuntimeException("database down"));

        Long result = service.persistTransfer(voucher, DOCUMENT_LINK);

        assertThat(result).isNull();
        verify(invoiceErrorService).logRequiredFieldError(
                "DVELOP",
                null,
                DOCUMENT_ID,
                null,
                "Persistence failure while saving invoice",
                4
        );
        verify(invoicePositionRepository, never()).saveAll(any());
    }

    @Test
    void buildProjectNumber_trimsCustom2AndExpandsCommaSeparatedCustom10() {
        InvoiceTransferLineItemDto lineItem = new InvoiceTransferLineItemDto();
        lineItem.setCustom2(" 100 ");
        lineItem.setCustom10("200,300");

        String result = service.buildProjectNumber(lineItem);

        assertThat(result).isEqualTo("100/200/300");
    }

    @Test
    void buildProjectNumber_whenRequiredCustomFieldsBlank_returnsNull() {
        InvoiceTransferLineItemDto lineItem = new InvoiceTransferLineItemDto();
        lineItem.setCustom2(" ");
        lineItem.setCustom10("200");

        assertThat(service.buildProjectNumber(lineItem)).isNull();
    }

    private InvoiceVoucherDto validVoucher() {
        InvoiceVoucherDto voucher = new InvoiceVoucherDto();
        voucher.setDocId(DOCUMENT_ID);
        voucher.setDocumentType(documentType("Invoice"));
        voucher.setCompany(company("C-1", "Commodis GmbH"));
        voucher.setVendor(vendor("V-1", "Acme GmbH"));
        voucher.setCurrency(currency("EUR"));
        voucher.setGrossAmount(new BigDecimal("119.00"));
        voucher.setNetAmount(new BigDecimal("100.00"));
        voucher.setVatAmount(new BigDecimal("19.00"));
        voucher.setDocumentDate(OffsetDateTime.of(2024, 1, 2, 0, 0, 0, 0, ZoneOffset.UTC));
        voucher.setInternalNumber("INT-1");
        voucher.setExternalNumber("EXT-1");
        voucher.setPostingText("Posting text");
        voucher.setCustom1("custom-1");
        voucher.setLineItems(Map.of("1", validLineItem()));
        return voucher;
    }

    private InvoiceTransferLineItemDto validLineItem() {
        InvoiceTransferUserDto verifier = new InvoiceTransferUserDto();
        verifier.setName(VERIFIER_EMAIL);
        InvoiceTransferTaxCodeDto taxCode = new InvoiceTransferTaxCodeDto();
        taxCode.setPercentage(19);
        InvoiceTransferQuantityDto quantity = new InvoiceTransferQuantityDto();
        quantity.setInvoiced(2);

        InvoiceTransferLineItemDto lineItem = new InvoiceTransferLineItemDto();
        lineItem.setInternalId("POS-1");
        lineItem.setLineNo(1);
        lineItem.setVerifiedBy(verifier);
        lineItem.setDescription("Consulting");
        lineItem.setGrossAmount(new BigDecimal("119.00"));
        lineItem.setNetAmount(new BigDecimal("100.00"));
        lineItem.setVatAmount(new BigDecimal("19.00"));
        lineItem.setTaxCode(taxCode);
        lineItem.setQuantity(quantity);
        lineItem.setCustom2("100");
        lineItem.setCustom10("200,300");
        return lineItem;
    }

    private InvoiceTransferDocumentTypeDto documentType(String name) {
        InvoiceTransferDocumentTypeDto dto = new InvoiceTransferDocumentTypeDto();
        dto.setName(name);
        return dto;
    }

    private InvoiceTransferCompanyDto company(String nr, String name) {
        InvoiceTransferCompanyDto dto = new InvoiceTransferCompanyDto();
        dto.setNr(nr);
        dto.setName(name);
        return dto;
    }

    private InvoiceTransferVendorDto vendor(String nr, String name) {
        InvoiceTransferVendorDto dto = new InvoiceTransferVendorDto();
        dto.setNr(nr);
        dto.setName(name);
        return dto;
    }

    private InvoiceTransferCurrencyDto currency(String code) {
        InvoiceTransferCurrencyDto dto = new InvoiceTransferCurrencyDto();
        dto.setCode(code);
        return dto;
    }
}
