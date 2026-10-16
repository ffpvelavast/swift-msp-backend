/** JPA entity representing the InvoiceSortBy table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.rex.commodis.exceptions.BadRequestException;

public enum InvoiceSortBy {

    VENDOR("vendor"),
    INVOICE_TITLE("documentName"),
    INVOICE_DATE("documentDate"),
    INVOICE_ID("id"),
    INVOICE_TOTAL("invoiceTotalGross");

    private final String dbField;

    InvoiceSortBy(String dbField) {
        this.dbField = dbField;
    }

    public String getDbField() {
        return dbField;
    }

    @JsonCreator
    public static InvoiceSortBy from(String value) {
        if (value == null || value.isBlank()) {
            return null; // let service apply default
        }

        return switch (value.trim().toLowerCase()) {
            case "vendor" -> VENDOR;
            case "invoice_title" -> INVOICE_TITLE;
            case "invoice_date" -> INVOICE_DATE;
            case "invoice_id" -> INVOICE_ID;
            case "invoice_total" -> INVOICE_TOTAL;
            default -> throw new IllegalArgumentException(
                    "Invalid sort_by: '" + value + "'. Allowed values: " +
                            "vendor, invoice_title, invoice_date, invoice_id, invoice_total"
            );
        };
    }
}