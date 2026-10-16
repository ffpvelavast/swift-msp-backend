/** REST controller exposing ExpenseReport API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.rex.commodis.dto.expenses.ExpenseReportCreateRequestDto;
import com.rex.commodis.dto.expenses.ExpenseReportCreateResponseDto;
import com.rex.commodis.security.user.RexUserContext;
import com.rex.commodis.service.ExpenseReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
@Tag(name = "Expense Reports", description = "APIs for managing expense reports")
public class ExpenseReportController {

    private final ExpenseReportService expenseReportService;

    @PostMapping("/create-expense-report")
    @Operation(
            summary = "Create a new expense report",
            description = "Creates a new expense report for the authenticated user. " +
                    "The report owner and creator are both derived from the JWT token."
    )
    public ResponseEntity<ExpenseReportCreateResponseDto> createExpenseReport(
            @Valid @RequestBody ExpenseReportCreateRequestDto requestDto,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {
        Long rexUserId = rexUserContext.getRexUserId();
//          Long rexUserId = 100L;
        ExpenseReportCreateResponseDto response =
                expenseReportService.createExpenseReport(requestDto, rexUserId);

        return ResponseEntity.ok(response);
    }
}
