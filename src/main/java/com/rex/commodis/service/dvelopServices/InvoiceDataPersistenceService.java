/** Service handling InvoiceDataPersistence operations; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service.dvelopServices;

import com.rex.commodis.dto.dvelopDtos.InvoiceTransferLineItemDto;
import com.rex.commodis.dto.dvelopDtos.InvoiceVoucherDto;
import com.rex.commodis.model.invoices.Invoice;
import com.rex.commodis.model.invoices.InvoicePosition;
import com.rex.commodis.repository.invoices.InvoicePositionRepository;
import com.rex.commodis.repository.invoices.InvoiceRepository;
import com.rex.commodis.service.ProjectNumberService;
import com.rex.commodis.service.invoiceServices.InvoiceErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@RequiredArgsConstructor
public class InvoiceDataPersistenceService {

    @Value("${dvelop.invoice.source-system}")
    private String sourceSystem;

    private final InvoiceErrorService invoiceErrorService;
    private final InvoiceRepository invoiceRepository;
    private final InvoicePositionRepository invoicePositionRepository;

    private final ProjectNumberService projectNumberService;
    private final InvoiceAssignmentMailService invoiceAssignmentMailService;
    private final InvoiceVerifierRexUserLookupService invoiceVerifierRexUserLookupService;

    @Transactional(propagation = REQUIRES_NEW)
    public Long persistTransfer(InvoiceVoucherDto voucherDto, String documentLink) {

        String verifierEmail = invoiceVerifierRexUserLookupService.getVerifierEmail(voucherDto);

        Invoice invoice = new Invoice();

        String documentTypeName = voucherDto.getDocumentType() != null
                ? voucherDto.getDocumentType().getName()
                : null;

        String companyName = voucherDto.getCompany() != null
                ? voucherDto.getCompany().getName()
                : null;

        String companyNr = voucherDto.getCompany() != null
                ? voucherDto.getCompany().getNr()
                : null;

        String vendorName = voucherDto.getVendor() != null
                ? voucherDto.getVendor().getName()
                : null;

        String vendorNr = voucherDto.getVendor() != null
                ? voucherDto.getVendor().getNr()
                : null;

        String currencyCode = voucherDto.getCurrency() != null
                ? voucherDto.getCurrency().getCode()
                : null;

        invoice.setDocumentNumber(voucherDto.getDocId());
        invoice.setDocumentName(documentTypeName);
        invoice.setDocumentType(documentTypeName);
        invoice.setDocumentLink(documentLink);
        invoice.setDocumentDate(toLocalDate(voucherDto.getDocumentDate()));
        invoice.setSupplyDate(toLocalDate(voucherDto.getDateOfSupply()));
        invoice.setPaymentDate(toLocalDate(voucherDto.getPaymentDate()));
        invoice.setAccountingText(voucherDto.getPostingText());
        invoice.setRecipientNumber(companyNr);
        invoice.setRecipient(companyName);
        invoice.setRecipientInvoiceNumber(voucherDto.getInternalNumber());
        invoice.setVendorNumber(vendorNr);
        invoice.setVendor(vendorName);
        invoice.setVendorInvoiceNumber(voucherDto.getExternalNumber());
        invoice.setInvoiceTotalGross(voucherDto.getGrossAmount());
        invoice.setInvoiceTotalNet(voucherDto.getNetAmount());
        invoice.setInvoiceTotalVat(voucherDto.getVatAmount());
        invoice.setCurrency(currencyCode);

        //Set Custom Fields
        invoice.setCustomField1(voucherDto.getCustom1());
        invoice.setCustomField2(voucherDto.getCustom2());
        invoice.setCustomField3(voucherDto.getCustom3());
        invoice.setCustomField4(voucherDto.getCustom4());
        invoice.setCustomField5(voucherDto.getCustom5());
        invoice.setCustomField6(voucherDto.getCustom6());
        invoice.setCustomField7(voucherDto.getCustom7());
        invoice.setCustomField8(voucherDto.getCustom8());
        invoice.setCustomField9(voucherDto.getCustom9());
        invoice.setCustomField10(voucherDto.getCustom10());
        invoice.setCustomField11(voucherDto.getCustom11());
        invoice.setCustomField12(voucherDto.getCustom12());
        invoice.setCustomField13(voucherDto.getCustom13());
        invoice.setCustomField14(voucherDto.getCustom14());
        invoice.setCustomField15(voucherDto.getCustom15());
        invoice.setCustomField16(voucherDto.getCustom16());
        invoice.setCustomField17(voucherDto.getCustom17());
        invoice.setCustomField18(voucherDto.getCustom18());
        invoice.setCustomField19(voucherDto.getCustom19());
        invoice.setCustomField20(voucherDto.getCustom20());

        //Log Error for Null Fields
        boolean isValidInvoice = validateInvoice(
                voucherDto.getDocId(), documentTypeName,
                companyName, vendorName, verifierEmail
        );

        //Do not log incomplete Invoice Data
        if(!isValidInvoice)
            return null;

        Map<String, InvoiceTransferLineItemDto> lineItems = voucherDto.getLineItems();
        List<InvoicePosition> positions = buildPositions(lineItems, voucherDto.getDocId(), verifierEmail);

        //Do not log incomplete Invoice_Position Data
        if(positions.isEmpty())
            return null;

        try{
            //Persist Invoice Data
            Invoice dbInvoice = invoiceRepository.save(invoice);

            //Load invoice_id in Invoice_Position
            positions.forEach(position -> position.setInvoiceId(dbInvoice.getId()));

            //Persist Invoice_Position Data
            invoicePositionRepository.saveAll(positions);

            return dbInvoice.getId();

        } catch (Exception e) {
            invoiceErrorService.logRequiredFieldError(
                    sourceSystem,
                    null,
                    voucherDto.getDocId(),
                    null,
                    "Persistence failure while saving invoice",
                    4
            );

            return null;
        }
    }

    private List<InvoicePosition> buildPositions(
            Map<String, InvoiceTransferLineItemDto> lineItems,
            String documentId,
            String verifierEmail
    ) {
        List<InvoicePosition> positions = new ArrayList<>(lineItems.size());

        for (InvoiceTransferLineItemDto lineItem : lineItems.values()) {

            InvoicePosition position = new InvoicePosition();

            String approverName = lineItem.getVerifiedBy() != null
                    ? lineItem.getVerifiedBy().getName()
                    : null;

            Integer vatRate = lineItem.getTaxCode() != null
                    ? lineItem.getTaxCode().getPercentage()
                    : null;

            Integer quantity = lineItem.getQuantity() != null
                    ? lineItem.getQuantity().getInvoiced()
                    : null;

            position.setExternalPositionNumber(lineItem.getInternalId());
            position.setPositionLineNumber(lineItem.getLineNo());
            position.setFactualApprover(approverName);
            position.setPositionText(lineItem.getDescription());
            position.setPositionVatRate(vatRate);
            position.setPositionQuantity(quantity);
            position.setPositionAmountGross(lineItem.getGrossAmount());
            position.setPositionAmountNet(lineItem.getNetAmount());
            position.setPositionAmountVat(lineItem.getVatAmount());

            //Set Custom Fields
            position.setCustomField1(lineItem.getCustom1());
            position.setCustomField2(lineItem.getCustom2());
            position.setCustomField3(lineItem.getCustom3());
            position.setCustomField4(lineItem.getCustom4());
            position.setCustomField5(lineItem.getCustom5());
            position.setCustomField6(lineItem.getCustom6());
            position.setCustomField7(lineItem.getCustom7());
            position.setCustomField8(lineItem.getCustom8());
            position.setCustomField9(lineItem.getCustom9());
            position.setCustomField10(lineItem.getCustom10());
            position.setCustomField11(lineItem.getCustom11());
            position.setCustomField12(lineItem.getCustom12());
            position.setCustomField13(lineItem.getCustom13());
            position.setCustomField14(lineItem.getCustom14());
            position.setCustomField15(lineItem.getCustom15());
            position.setCustomField16(lineItem.getCustom16());
            position.setCustomField17(lineItem.getCustom17());
            position.setCustomField18(lineItem.getCustom18());
            position.setCustomField19(lineItem.getCustom19());
            position.setCustomField20(lineItem.getCustom20());

            //Build ProjectNumbers & Retrieve Ids for Invoice_Positions
            String projectNumber = buildProjectNumber(lineItem);
            Long projectNumberId = projectNumberService.resolveProjectNumberId(documentId, projectNumber);

            position.setProjectNumber(projectNumber);
            position.setProjectNumberId(projectNumberId);

            //Log Error for Null Fields
            boolean isValidInvoicePosition = validateInvoicePositions(lineItem, approverName, documentId, verifierEmail);

            if(!isValidInvoicePosition)
                return Collections.emptyList();

            positions.add(position);
        }

        return positions;
    }

    //HELPER METHODS

    private LocalDate toLocalDate(OffsetDateTime value) {
        return value != null ? value.toLocalDate() : null;
    }

    private boolean validateInvoice(
            String documentId,
            String documentTypeName,
            String companyName,
            String vendorName,
            String verifierEmail
    ) {
        boolean isValidInvoice = true;

        if (documentTypeName == null) {
            invoiceErrorService.logRequiredFieldError(
                    "INVOICES",
                    "document_type.name",
                    documentId,
                    null,
                    "Required field is missing",
                    1);
            invoiceAssignmentMailService.sendMissingRequiredFieldEmail("INVOICES", "document_type.name");
            if (verifierEmail != null)
                invoiceAssignmentMailService.sendInvoiceAssignmentFailureMail(verifierEmail, "INVOICES", "document_type.name");
            isValidInvoice = false;
        }

        if (documentId == null) {
            invoiceErrorService.logRequiredFieldError(
                    "INVOICES",
                    "doc_id",
                    null,
                    null,
                    "Required field is missing",
                    1);
            invoiceAssignmentMailService.sendMissingRequiredFieldEmail("INVOICES", "doc_id");
            if (verifierEmail != null)
                invoiceAssignmentMailService.sendInvoiceAssignmentFailureMail(verifierEmail, "INVOICES", "doc_id");
            isValidInvoice = false;
        }

        if (companyName == null) {
            invoiceErrorService.logRequiredFieldError(
                    "INVOICES",
                    "company.name",
                    documentId,
                    null,
                    "Required field is missing",
                    1);
            invoiceAssignmentMailService.sendMissingRequiredFieldEmail("INVOICES", "company.name");
            if (verifierEmail != null)
                invoiceAssignmentMailService.sendInvoiceAssignmentFailureMail(verifierEmail, "INVOICES", "company.name");
            isValidInvoice = false;
        }

        if (vendorName == null) {
            invoiceErrorService.logRequiredFieldError(
                    "INVOICES",
                    "vendor.name",
                    documentId,
                    null,
                    "Required field is missing",
                    1);
            invoiceAssignmentMailService.sendMissingRequiredFieldEmail("INVOICES", "vendor.name");
            if (verifierEmail != null)
                invoiceAssignmentMailService.sendInvoiceAssignmentFailureMail(verifierEmail, "INVOICES", "vendor.name");
            isValidInvoice = false;
        }

        return isValidInvoice;
    }

    private boolean validateInvoicePositions(
            InvoiceTransferLineItemDto lineItem,
            String approverName,
            String documentId,
            String verifierEmail
    ) {

        boolean isValidInvoicePosition = true;

        if (lineItem.getInternalId() == null) {
            invoiceErrorService.logRequiredFieldError(
                    "INVOICE_POSITIONS",
                    "internal_id",
                    documentId,
                    null,
                    "Required field is missing",
                    1
            );
            invoiceAssignmentMailService.sendMissingRequiredFieldEmail("INVOICE_POSITIONS", "internal_id");
            if (verifierEmail != null)
                invoiceAssignmentMailService.sendInvoiceAssignmentFailureMail(verifierEmail, "INVOICE_POSITIONS", "internal_id");
            isValidInvoicePosition = false;
        }

        if (approverName == null) {
            invoiceErrorService.logRequiredFieldError(
                    "INVOICE_POSITIONS",
                    "verified_by.name",
                    documentId,
                    null,
                    "Required field is missing",
                    1
            );
            invoiceAssignmentMailService.sendMissingRequiredFieldEmail("INVOICE_POSITIONS", "verified_by.name");
            if (verifierEmail != null)
                invoiceAssignmentMailService.sendInvoiceAssignmentFailureMail(verifierEmail, "INVOICE_POSITIONS", "verified_by.name");
            isValidInvoicePosition = false;
        }

        if (lineItem.getDescription() == null) {
            invoiceErrorService.logRequiredFieldError(
                    "INVOICE_POSITIONS",
                    "description",
                    documentId,
                    null,
                    "Required field is missing",
                    1
            );
            invoiceAssignmentMailService.sendMissingRequiredFieldEmail("INVOICE_POSITIONS", "description");
            if (verifierEmail != null)
                invoiceAssignmentMailService.sendInvoiceAssignmentFailureMail(verifierEmail, "INVOICE_POSITIONS", "description");
            isValidInvoicePosition = false;
        }

        return isValidInvoicePosition;
    }

    public String buildProjectNumber(InvoiceTransferLineItemDto invoiceTransferLineItemDto) {

        String custom2 = invoiceTransferLineItemDto.getCustom2();
        String custom10 = invoiceTransferLineItemDto.getCustom10();

        if (custom2 == null || custom2.isBlank()
                || custom10 == null || custom10.isBlank()) {
            return null;
        }

        String projectNumber =
                custom2.trim() + "/" + custom10.trim().replace(",", "/");

        return projectNumber;
    }
}



