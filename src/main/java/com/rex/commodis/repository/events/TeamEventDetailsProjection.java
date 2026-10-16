/** Spring Data JPA repository for TeamEventDetailsProjection entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.events;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TeamEventDetailsProjection {

    Long getTeamEventId();

    LocalDate getEventDate();

    String getEventTitle();

    String getInternalNote();

    String getSubmittedBy();

    String getProjectNumber();

    Long getReceiptId();

    String getReceiptTitle();

    LocalDate getReceiptDate();

    String getReceiptNumber();

    String getSubmittedFor();

    String getReceiptStatus();

    BigDecimal getReceiptAmount();

    String getReceiptCurrency();

    BigDecimal getGratuity();

    BigDecimal getAmountNetFullVat();

    Short getVatRateFull();

    String getExpenseCategory();

    String getExpenseNature();

    String getIssuanceRegion();

    Boolean getDeductTeamBalance();

    Integer getFileSequence();

    String getFileUrl();

    Long getTeameventtypeId();

    LocalDate getBookableThrough();

    String getStatus();

    Long getProjectNumberId();

    Long getPicklistId();

    Long getPicklistMemberCount();
}