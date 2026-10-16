/** Service interface defining ReceiptGetListApproverProjectionDto operations; implemented by ReceiptGetListApproverProjectionDtoImpl. */
package com.rex.commodis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * JPQL projection for the receipt listing query.
 *
 * NOTE on "category":
 *   The schema does not have an explicit category column on receipts.receipts.
 *   The issuance_region table (region_code / display_text) is used as the category.
 *   getCategory() maps to ir.regionCode from the JPQL query.
 *   Adjust to ir.displayText if you prefer the localised label.
 */
public interface ReceiptGetListApproverProjectionDto {
    Long getId();
    String getReceiptTitle();
    String getSubmittedFor();      // rexuser.listname of the receipt owner
    LocalDate getReceiptDate();
    String getCategory();         // issuance_region.region_code
    String getReceiptNumber();    // receipts.receiptnumber
    BigDecimal getReceiptAmount();
    String getCurrency();         // hard-coded "EUR" in query; extend if needed
    String getStatus();           // status label from receipt_status.display_text or enum name
    String category();// status label from receipt_status.display_text or enum name
}