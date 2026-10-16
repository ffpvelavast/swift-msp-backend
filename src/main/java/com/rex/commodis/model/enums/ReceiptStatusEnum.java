/** JPA entity representing the ReceiptStatusEnum table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.enums;

public enum ReceiptStatusEnum {
    SUBMITTED,
    CORRECTION_REQUESTED,
    RESUBMITTED,
    REJECTED,
    APPROVED,
    WITHDRAWN
}
