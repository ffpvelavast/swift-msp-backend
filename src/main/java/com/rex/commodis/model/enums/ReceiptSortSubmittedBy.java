/** JPA entity representing the ReceiptSortSubmittedBy table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReceiptSortSubmittedBy{

    RECEIPT_DATE("receiptDate"),
    RECEIPT_TITLE("receiptTitle"),
    RECEIPT_AMOUNT("receiptAmount"),
    SUBMITTED_BY("submittedby"),
    CATEGORY("category"),
    STATUS("status");

    private final String field;

    ReceiptSortSubmittedBy(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    @JsonCreator
    public static ReceiptSortSubmittedBy from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = value.trim();

        try {
            return ReceiptSortSubmittedBy.valueOf(normalized.toUpperCase());
        } catch (IllegalArgumentException ignored) {
        }

        return switch (normalized.toLowerCase()) {
            case "receipt_date" -> RECEIPT_DATE;
            case "receipt_title" -> RECEIPT_TITLE;
            case "receipt_amount" -> RECEIPT_AMOUNT;
            case "submitted_by" -> SUBMITTED_BY;
            case "category" -> CATEGORY;
            case "status" -> STATUS;
            default -> throw new IllegalArgumentException(
                    "Invalid sort_by: '" + value + "'. Allowed values: " +
                            "RECEIPT_DATE, RECEIPT_TITLE, RECEIPT_AMOUNT, RECEIPT_NUMBER, SUBMITTED_BY, CATEGORY, STATUS");
        };
    }
}