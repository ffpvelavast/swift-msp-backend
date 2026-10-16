/** Spring Data JPA repository for ExpenseReport entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.expenses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.expenses.ExpenseReport;

import java.util.List;

@Repository
public interface ExpenseReportRepository extends JpaRepository<ExpenseReport, Long> {

    @Query(value = """
        SELECT id AS id,
               report_title::text AS reportTitle,
               reportdate_from AS reportDateFrom,
               reportdate_through AS reportDateThrough
        FROM expenses.select_expense_report(:rexUserId)
        """, nativeQuery = true)
    List<ExpenseReportProjection> findExpenseReports(@Param("rexUserId") Long rexUserId);
}