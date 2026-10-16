/** Service handling InvoiceVerifierRexUserLookup operations; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service.dvelopServices;

import com.rex.commodis.dto.dvelopDtos.InvoiceTransferLineItemDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceTransferUserDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceVoucherDto;
import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.enums.InvoiceStatusEnum;
import com.rex.commodis.service.invoiceServices.InvoiceErrorService;
import com.rex.commodis.service.invoiceServices.InvoiceHistoryService;
import com.rex.commodis.service.RexUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceVerifierRexUserLookupService {

    @Value("${dvelop.invoice.source-system}")
    private String sourceSystem;

    private final RexUserService rexUserService;
    private final InvoiceHistoryService invoiceHistoryService;
    private final InvoiceErrorService invoiceErrorService;
    private final InvoiceAssignmentMailService invoiceAssignmentMailService;

    public RexUser resolveRexUserVerifier(InvoiceVoucherDto invoiceVoucherDto, Long invoiceId) {

        String verifierEmail = getVerifierEmail(invoiceVoucherDto);

        //Find Associated RexUser
        Optional<RexUser> dbRexUser = rexUserService.findByEmailIgnoreCase(verifierEmail);

        return dbRexUser.orElseGet(() -> {
            invoiceErrorService.logRexUserLookupError(
                    invoiceId,
                    verifierEmail,
                    "NOT_FOUND",
                    "RexUser lookup failed",
                    2
            );


            // Insert ERROR into invoice history
            invoiceHistoryService.createStatus(invoiceId, InvoiceStatusEnum.ERROR, sourceSystem);

            // Send Error Mail to Admin
            invoiceAssignmentMailService.sendMissingRexUserEmail(verifierEmail);

            return null;
        });
    }

    public String getVerifierEmail(InvoiceVoucherDto voucherDto){
        Map<String, InvoiceTransferLineItemDto> lineItems = voucherDto.getLineItems();
        InvoiceTransferLineItemDto lineItem = lineItems.values().iterator().next();
        InvoiceTransferUserDto verifiedBy = lineItem.getVerifiedBy();
        return verifiedBy != null ? verifiedBy.getName() : null;
    }
}


