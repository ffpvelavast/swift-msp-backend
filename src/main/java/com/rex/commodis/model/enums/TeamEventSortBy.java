/** JPA entity representing the TeamEventSortBy table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TeamEventSortBy{

    EVENT_DATE("eventDate"),
    EVENT_TITLE("eventTitle"),
    SUBMITTED_BY("submittedBy"),
    PROJECT_NUMBER("projectNumber"),
    TOTAL_AMOUNT("totalAmount"),
    STATUS("status");

    private final String dbField;

    TeamEventSortBy(String dbField) {
        this.dbField = dbField;
    }

    public String getDbField() {
        return dbField;
    }

    @JsonCreator
    public static TeamEventSortBy from(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return switch (value.trim().toLowerCase()) {

            case "event_date" -> EVENT_DATE;
            case "event_title" -> EVENT_TITLE;
            case "submitted_by" -> SUBMITTED_BY;
            case "project_number" -> PROJECT_NUMBER;
            case "total_amount" -> TOTAL_AMOUNT;
            case "status" -> STATUS;

            default -> throw new IllegalArgumentException(
                    "Invalid sort_by: '" + value +
                            "'. Allowed values: event_date,event_title," +
                            "submitted_by,project_number,total_amount,status"
            );
        };
    }
}