package com.rex.commodis.service.dvelopServices;

import com.rex.commodis.dto.dvelopDtos.InvoiceTransferLineItemDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferUserDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceVoucherDto;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.enums.InvoiceStatusEnum;
import com.rex.commodis.service.RexUserService;
import com.rex.commodis.service.invoiceServices.InvoiceErrorService;
import com.rex.commodis.service.invoiceServices.InvoiceHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceVerifierRexUserLookupServiceTest {

    @Mock private RexUserService rexUserService;
    @Mock private InvoiceHistoryService invoiceHistoryService;
    @Mock private InvoiceErrorService invoiceErrorService;
    @Mock private InvoiceAssignmentMailService invoiceAssignmentMailService;

    @InjectMocks
    private InvoiceVerifierRexUserLookupService service;

    private static final Long INVOICE_ID = 10L;
    private static final String VERIFIER_EMAIL = "verifier@example.com";

    @Test
    void getVerifierEmail_returnsVerifiedByNameFromFirstLineItem() {
        InvoiceVoucherDto voucher = voucherWithVerifier(VERIFIER_EMAIL);

        String result = service.getVerifierEmail(voucher);

        assertThat(result).isEqualTo(VERIFIER_EMAIL);
    }

    @Test
    void resolveRexUserVerifier_whenUserExists_returnsUser() {
        InvoiceVoucherDto voucher = voucherWithVerifier(VERIFIER_EMAIL);
        RexUser rexUser = new RexUser();
        rexUser.setId(99L);
        rexUser.setEmail(VERIFIER_EMAIL);
        when(rexUserService.findByEmailIgnoreCase(VERIFIER_EMAIL))
                .thenReturn(Optional.of(rexUser));

        RexUser result = service.resolveRexUserVerifier(voucher, INVOICE_ID);

        assertThat(result).isSameAs(rexUser);
        verify(rexUserService).findByEmailIgnoreCase(VERIFIER_EMAIL);
        verifyNoInteractions(invoiceErrorService, invoiceHistoryService, invoiceAssignmentMailService);
    }

    @Test
    void resolveRexUserVerifier_whenUserMissing_logsErrorCreatesErrorHistoryAndSendsMail() {
        InvoiceVoucherDto voucher = voucherWithVerifier(VERIFIER_EMAIL);
        when(rexUserService.findByEmailIgnoreCase(VERIFIER_EMAIL))
                .thenReturn(Optional.empty());

        RexUser result = service.resolveRexUserVerifier(voucher, INVOICE_ID);

        assertThat(result).isNull();
        verify(invoiceErrorService).logRexUserLookupError(
                INVOICE_ID,
                VERIFIER_EMAIL,
                "NOT_FOUND",
                "RexUser lookup failed",
                2
        );
        verify(invoiceHistoryService).createStatus(INVOICE_ID, InvoiceStatusEnum.ERROR, null);
        verify(invoiceAssignmentMailService).sendMissingRexUserEmail(VERIFIER_EMAIL);
    }

    private InvoiceVoucherDto voucherWithVerifier(String verifierEmail) {
        InvoiceTransferUserDto user = new InvoiceTransferUserDto();
        user.setName(verifierEmail);

        InvoiceTransferLineItemDto lineItem = new InvoiceTransferLineItemDto();
        lineItem.setVerifiedBy(user);

        InvoiceVoucherDto voucher = new InvoiceVoucherDto();
        voucher.setLineItems(Map.of("1", lineItem));
        return voucher;
    }
}
