/** JPA entity representing the TeamEventStatusEnum table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.enums;

public enum TeamEventStatusEnum {
    SUBMITTED,
    CORRECTION_REQUESTED,
    RESUBMITTED,
    REJECTED,
    APPROVED,
    WITHDRAWN
}
