/** REST controller exposing Dashboard API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.rex.commodis.dto.DashboardAllCountsResponseDto;
import com.rex.commodis.dto.events.DashboardTeamEventCountResponseDto;
import com.rex.commodis.dto.events.DashboardTeamEventListResponseDto;
import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardCountDto;
import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardListResponseDto;
import com.rex.commodis.dto.receipts.DashboardApproverReceiptListResponseDto;
import com.rex.commodis.dto.receipts.DashboardReceiptApproverCountResponseDto;
import com.rex.commodis.dto.receipts.SubmitterCountDto;
import com.rex.commodis.dto.receipts.SubmitterReceiptDto;
import com.rex.commodis.security.user.RexUserContext;
import com.rex.commodis.service.*;
import com.rex.commodis.service.invoiceServices.InvoiceDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "APIs for the Dashboard screen")
public class DashboardController {

    private final InvoiceDashboardService invoiceDashboardService;
    private final DashboardReceiptApproverService dashboardReceiptService;
    private final DashboardApproverReceiptListService service;
    private final TeamEventApproverCountService teamEventApproverCountService;
    private final DashboardTeamEventListService teamEventListService;
    private final SubmitterCountService submitterCountService;
    private final SubmitterReceiptService submitterReceiptService;

    @GetMapping("/invoices/count")
    @Operation(
            summary = "Get dashboard invoice count",
            description = "Returns the total count of invoices with PENDING or DRAFT status " +
                    "where the logged-in user is the delegate."
    )
    public ResponseEntity<InvoiceDashboardCountDto> getDashboardInvoicesCount(
            @AuthenticationPrincipal RexUserContext rexUserContext) {

         Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(invoiceDashboardService.getDashboardInvoicesCount(rexUserId));
    }

    @GetMapping("/invoices/list")
    @Operation(
            summary = "Get dashboard invoice list",
            description = "Returns the latest 5 invoices with PENDING or DRAFT status " +
                    "where the logged-in user is the delegate, ordered by created date descending."
    )
    public ResponseEntity<InvoiceDashboardListResponseDto> getDashboardInvoicesList(
            @AuthenticationPrincipal RexUserContext rexUserContext) {

        Long rexUserId = rexUserContext.getRexUserId();


        return ResponseEntity.ok(invoiceDashboardService.getDashboardInvoicesList(rexUserId));
    }

    @GetMapping("receipts/approver-list")
    @Operation(
            summary = "Get dashboard approver receipt list",
            description = "Returns latest 5 receipts assigned to logged-in approver " +
                    "with statuses SUBMITTED, RESUBMITTED, or CORRECTION_REQUESTED"
    )
    public ResponseEntity<DashboardApproverReceiptListResponseDto>
    getDashboardApproverReceiptList(
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(
                service.getDashboardApproverReceiptList(rexUserId)
        );
    }

    @GetMapping("receipts/approver-count")
    @Operation(
            summary = "Get dashboard approver receipt count",
            description = "Returns count of receipts assigned to logged-in approver " +
                    "having statuses SUBMITTED, RESUBMITTED, or CORRECTION_REQUESTED"
    )
    public ResponseEntity<DashboardReceiptApproverCountResponseDto> getApproverReceiptCount(
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(
                dashboardReceiptService.getApproverReceiptCount(rexUserId)
        );
    }

    @GetMapping("receipts/submitter-list")
    @Operation(
            summary = "Get latest 5 submitter receipts",
            description = "Fetches latest 5 receipts for logged-in submitter filtered by status"
    )
    public ResponseEntity<List<SubmitterReceiptDto>> getSubmitterReceipts(
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        Long rexUserId = rexUserContext.getRexUserId();
        return ResponseEntity.ok(
                submitterReceiptService.getSubmitterReceipts(rexUserId)
        );
    }

    @GetMapping("receipts/submitter-count")
    public ResponseEntity<SubmitterCountDto> getSubmitterReceiptCount(
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        Long rexUserId = rexUserContext.getRexUserId();
        return ResponseEntity.ok(
                submitterCountService.getSubmitterReceiptCount(rexUserId)
        );
    }


    @GetMapping("team-events/list")
    @Operation(
            summary = "Get dashboard team event list"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<DashboardTeamEventListResponseDto> getDashboardTeamEventList(
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        Long rexUserId = rexUserContext.getRexUserId();

        DashboardTeamEventListResponseDto response =
                teamEventListService.getTeamEventListForApprover(rexUserId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("team-events/count")
    @Operation(
            summary = "Get dashboard team event approver count"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Count fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<DashboardTeamEventCountResponseDto> getTeamEventApproverCount(
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        Long rexUserId = rexUserContext.getRexUserId();

        DashboardTeamEventCountResponseDto response =
                teamEventApproverCountService.getTeamEventApproverCount(rexUserId);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/counts")
    @Operation(
            summary = "Get all dashboard counts",
            description = "Returns all dashboard counts for logged-in user"
    )
    public ResponseEntity<DashboardAllCountsResponseDto> getAllDashboardCounts(
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        Long rexUserId = rexUserContext.getRexUserId();


        DashboardAllCountsResponseDto response =
                DashboardAllCountsResponseDto.builder()

                        .invoiceCount(
                                invoiceDashboardService
                                        .getDashboardInvoicesCount(rexUserId)

                        )

                        .approverReceiptCount(
                                dashboardReceiptService
                                        .getApproverReceiptCount(rexUserId)
                        )

                        .submitterReceiptCount(
                                submitterCountService
                                        .getSubmitterReceiptCount(rexUserId)
                        )

                        .teamEventCount(
                                teamEventApproverCountService
                                        .getTeamEventApproverCount(rexUserId)

                        )

                        .build();

        return ResponseEntity.ok(response);
    }

}
