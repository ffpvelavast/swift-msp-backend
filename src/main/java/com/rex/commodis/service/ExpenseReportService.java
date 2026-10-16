/** Service interface defining ExpenseReport operations; implemented by ExpenseReportServiceImpl. */
package com.rex.commodis.service;

import com.rex.commodis.dto.expenses.ExpenseReportCreateRequestDto;
import com.rex.commodis.dto.expenses.ExpenseReportCreateResponseDto;

public interface ExpenseReportService {

    ExpenseReportCreateResponseDto createExpenseReport(ExpenseReportCreateRequestDto requestDto, Long rexUserId);
}
