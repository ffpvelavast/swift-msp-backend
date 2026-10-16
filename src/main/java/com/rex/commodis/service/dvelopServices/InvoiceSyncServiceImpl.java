/** Service implementation for InvoiceSync business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service.dvelopServices;

import java.time.OffsetDateTime;
import java.util.Set;

import com.rex.commodis.dto.dvelopDtos.*;
import com.rex.commodis.service.invoiceServices.InvoiceHistoryService;
import com.rex.commodis.service.invoiceServices.InvoicePositionService;
import org.springframework.stereotype.Service;

import com.rex.commodis.model.RexUser;
import com.rex.commodis.model.enums.InvoiceStatusEnum;
import com.rex.commodis.service.invoiceServices.InvoiceResponsibilityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceSyncServiceImpl implements InvoiceSyncService {

    private final DvelopInvoiceClient dvelopInvoiceClient;
    private final InvoiceVerifierRexUserLookupService invoiceVerifierRexUserLookupService;
    private final InvoiceAssignmentMailService invoiceAssignmentMailService;
    private final InvoiceDataPersistenceService invoiceDataPersistenceService;

    private final InvoicePositionService invoicePositionService;
    private final InvoiceHistoryService invoiceHistoryService;
    private final InvoiceResponsibilityService invoiceResponsibilityService;

    @Override
    public void syncInvoices() {
        InvoiceTransfersResponseDto response = dvelopInvoiceClient.fetchTransfers();

        if (response == null ||
                response.getTransfers() == null ||
                response.getTransfers().isEmpty()) {
            log.info("No invoices found to fetch from Dvelop");
        }

        for (InvoiceTransferDto transfer : response.getTransfers()) {

            //Send POST API Call on Success
            acknowledgeTransfer(transfer);

            //Check Invoice for processing
            if(!checkInvoiceTransfer(transfer))
                continue;

            processTransfer(transfer);
        }
    }

    private void processTransfer(InvoiceTransferDto transfer) {
        log.info("Starting D.velop Invoice Processing");

        String documentLink = getDocumentLink(transfer);

        try {
            //Get Voucher
            InvoiceVoucherDto voucher = transfer.getWorkflow().getVoucher();

            //Persist data in invoices/invoice_positions
            Long invoiceId = invoiceDataPersistenceService.persistTransfer(voucher, documentLink);

            if(invoiceId == null){
                log.info("No invoice or invoice_position data was persisted.");
                return;
            }

            log.info("Invoice connected to {} was persisted with id {}", voucher.getDocId(), invoiceId);

            //Find ProjectNumbers
            Set<String> projectNumbers = invoicePositionService.findDistinctProjectNumbers(invoiceId);

            //Check verified_by for RexUser
            //Get verifiedBy Email
            RexUser dbRexUser = invoiceVerifierRexUserLookupService.resolveRexUserVerifier(voucher, invoiceId);

            if(dbRexUser != null){
                //Persist data in invoice_status/invoice_responsibility
                OffsetDateTime now = OffsetDateTime.now();

                //Assign Status in History
                invoiceHistoryService.createStatus(invoiceId, InvoiceStatusEnum.PENDING, dbRexUser.getFullName());

                //Assign Responsibility
                invoiceResponsibilityService.createResponsibility(invoiceId, dbRexUser.getId(), now);

                //Send email to user
                invoiceAssignmentMailService.sendAssignmentMail(dbRexUser.getEmail(), dbRexUser.getFullName(), projectNumbers, documentLink);
            }

            log.info("Invoice from : {} successfully persisted", voucher.getDocId());

        } catch (Exception exception) {
            log.error("Failed to process invoice: ", exception);
        }
    }

    private boolean checkInvoiceTransfer(InvoiceTransferDto invoiceTransferDto){

        String custom10 = invoiceTransferDto.getWorkflow().getVoucher().getCustom10();

        if(custom10 == null || custom10.isBlank())
            return true;

        return !custom10.trim().startsWith("0 |");
    }

    private void acknowledgeTransfer(InvoiceTransferDto transfer) {

        String callbackUrl = transfer.getLinks()
                .getReportResultsAsync()
                .getHref()
                .trim();

        log.info("D.Velop Success callback URL: {}", callbackUrl);

        dvelopInvoiceClient.reportSuccess(callbackUrl);
    }

    private String getDocumentLink(InvoiceTransferDto transfer) {
        return transfer.getLinks() != null && transfer.getLinks().getDmsObject() != null
                ? transfer.getLinks().getDmsObject().getHref()
                : null;
    }
}



