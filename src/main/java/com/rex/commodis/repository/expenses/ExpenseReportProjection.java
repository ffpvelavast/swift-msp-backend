/** Spring Data JPA repository for ExpenseReportProjection entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.expenses;

import java.time.LocalDate;

public interface ExpenseReportProjection {
    Long getId();
    String getReportTitle();
    LocalDate getReportDateFrom();
    LocalDate getReportDateThrough();
}
