/** JPA entity representing the InvoiceStatusEnum table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.enums;

public enum InvoiceStatusEnum {
    ERROR,
    DRAFT,
    PENDING,
    RELEASED,
    PROCESSED,
    REJECTED
}
