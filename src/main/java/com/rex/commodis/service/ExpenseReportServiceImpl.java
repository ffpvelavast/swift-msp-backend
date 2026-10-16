/** Service implementation for ExpenseReport business logic; called by the corresponding controller and delegates to the repository layer. */
package com.rex.commodis.service;

import com.rex.commodis.dto.expenses.ExpenseReportCreateRequestDto;
import com.rex.commodis.dto.expenses.ExpenseReportCreateResponseDto;
import com.rex.commodis.model.expenses.ExpenseReport;
import com.rex.commodis.repository.expenses.ExpenseReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseReportServiceImpl implements ExpenseReportService {

    private final ExpenseReportRepository expenseReportRepository;

    @Override
    @Transactional
    public ExpenseReportCreateResponseDto createExpenseReport(ExpenseReportCreateRequestDto requestDto, Long rexUserId) {

        ExpenseReport expenseReport = ExpenseReport.builder()
                .rexUserId(rexUserId)
                .creatorId(rexUserId)
                .reportTitle(requestDto.getReportTitle())
                .reportDateFrom(requestDto.getReportDateFrom())
                .reportDateThrough(requestDto.getReportDateThrough())
                .build();

        ExpenseReport saved = expenseReportRepository.save(expenseReport);

        return ExpenseReportCreateResponseDto.builder()
                .expenseReportId(saved.getId())
                .build();
    }
}
