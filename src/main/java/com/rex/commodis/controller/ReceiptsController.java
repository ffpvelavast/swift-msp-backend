/** REST controller exposing Receipts API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.rex.commodis.dto.receipts.CreateReceiptResponseDto;
import com.rex.commodis.model.enums.ReceiptSortApproverBy;
import com.rex.commodis.model.enums.ReceiptSortSubmittedBy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.rex.commodis.dto.*;
import com.rex.commodis.dto.receipts.CreateReceiptRequestDto;
import com.rex.commodis.dto.receipts.*;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.security.user.RexUserContext;
import com.rex.commodis.service.*;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
@Tag(name = "Receipt Details", description = "APIs for the Receipt Details for individual receipt screen")
public class ReceiptsController {

    private final ReceiptDetailsService receiptDetailsService;
    private final ReceiptSidePanelService receiptSidePanelService;
    private final ReceiptStatusUpdateService receiptStatusUpdateService;
    private final ReceiptDropDownService receiptDropDownService;
    private final ReceiptNotesService receiptNotesService;
    private final ReceiptsListingTableService receiptsService;
    private final ReceiptCountService receiptCountService;
    private final ReceiptService receiptService;

    @PostMapping(value = "/create-receipt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Create a new receipt",
            description = "Creates a new receipt with associated metadata and uploads one or more files. " +
                    "The request should be sent as multipart/form-data with receipt data in the 'data' part " +
                    "and files in the 'files' part.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            encoding = {
                                    @Encoding(name = "data", contentType = MediaType.APPLICATION_JSON_VALUE)
                            }
                    )
            )
    )
    public ResponseEntity<CreateReceiptResponseDto> createReceipt(
            @Valid @RequestPart("data") CreateReceiptRequestDto createReceiptRequestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        Long platformId = rexUserContext.getPlatformId();
        Long rexUserId = rexUserContext.getRexUserId();

        CreateReceiptResponseDto response =
                receiptService.createReceipt(createReceiptRequestDto, platformId, rexUserId, files);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{receiptId}/receipt-details")
    @Operation(
            summary = "Get individual receipt details",
            description = "Returns full receipt details for the approver screen — " +
                    "includes header info, status, expense data, location, and beneficiaries"
    )
    public ResponseEntity<ReceiptDetailResponseDto> getReceiptDetails(
            @Parameter(description = "ID of the receipt to retrieve", required = true)
            @PathVariable Long receiptId,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {
    	
    	Long rexUserId = rexUserContext.getRexUserId();

        ReceiptDetailResponseDto response =
                receiptDetailsService.getReceiptDetails(receiptId, rexUserId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{receiptId}/receipt-side-panel")
    @Operation(
            summary = "Get receipt side panel data",
            description = "Retrieves summary and contextual information for the receipt side panel view, " +
                    "such as status, key metadata, and related details."
    )
    public ResponseEntity<ReceiptSidePanelResponseDto> getSidePanel(
            @Parameter(description = "ID of the receipt to retrieve side panel data for", required = true)
            @PathVariable Long receiptId,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(
                receiptSidePanelService.getReceiptSidePanel(receiptId, rexUserId)
        );
    }
    
    @PostMapping("/{receiptId}/receipt-status")
    @Operation(
            summary = "Update receipt status",
            description = """
                    Updates the receipt status based on the approver's UI button action.
                    
                    UI Button → Status mapping:
                      Approve             → APPROVED
                      Reject              → REJECTED
                      Correction Requested → CORRECTION_REQUESTED
                    
                    Internally:
                      1. Deactivates the current active receipt_history row
                      2. Inserts a new row with active = true and the new status
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status value or receipt ID mismatch"),
            @ApiResponse(responseCode = "404", description = "Receipt not found or status not configured in DB")
    })
    public ResponseEntity<ReceiptStatusUpdateResponseDto> updateReceiptStatus(
            @Parameter(description = "ID of the receipt to update", required = true)
            @PathVariable Long receiptId,

            @Valid @RequestBody ReceiptStatusUpdateRequestDto receiptStatusUpdateRequestDto,

            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {
    	
    	Long rexUserId = rexUserContext.getRexUserId();
    	
        ReceiptStatusUpdateResponseDto response =
                receiptStatusUpdateService.updateReceiptStatus(receiptStatusUpdateRequestDto, receiptId, rexUserId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{receiptId}/receipt-notes")
    @Operation(
            summary = "Update receipt notes",
            description = "Updates the notes associated with a specific receipt. " +
                    "This allows users to modify or add additional information to an existing receipt."
    )
    public ResponseEntity<ReceiptUpdateNotesResponseDto> updateReceiptNotes(
            @Parameter(description = "ID of the receipt to update notes for", required = true)
            @PathVariable Long receiptId,

            @Parameter(description = "Request body containing updated receipt notes", required = true)
            @Valid @RequestBody ReceiptUpdateNotesRequestDto notesRequest,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {
        Long rexUserId = rexUserContext.getRexUserId();
        return ResponseEntity.ok(
                receiptNotesService.updateReceiptNotes(receiptId, rexUserId, notesRequest)
        );
    }

    @GetMapping("/receipt-dropdowns")
    @Operation(
            summary = "Get data for all drop down boxes",
            description = "Returns the data required for the output of the drop downs located at the" +
                    "top & middle bar of the screen"
    )
    public ResponseEntity<ReceiptDropDownResponseDto> getReceiptsDropdownData(
            @AuthenticationPrincipal RexUserContext rexUserContext
    ){

        Long rexUserId = rexUserContext.getRexUserId();
        Long platformId = rexUserContext.getPlatformId();

        return ResponseEntity.ok(receiptDropDownService.getDropDowns(rexUserId, platformId));
    }

    @GetMapping("/receipt-counts/submitter")
    @Operation(
            summary = "Get receipt status counts for the submitter",
            description = "Returns the total number of receipts and a breakdown of counts by receipt status " +
                    "for the currently authenticated user. The response includes all possible statuses, " +
                    "with counts defaulting to 0 where no records exist."
    )
    public ResponseEntity<ReceiptStatusCountDto> getSubmitterReceiptCounts(
            @AuthenticationPrincipal RexUserContext rexUserContext) {

        Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(receiptCountService.getSubmitterReceiptCounts(rexUserId));
    }

    @GetMapping("/receipt-counts/approver")
    @Operation(
            summary = "Get receipt status counts for the approver",
            description = "Returns the total number of receipts and a breakdown of counts by receipt status " +
                    "for the currently authenticated user. The response includes all possible statuses, " +
                    "with counts defaulting to 0 where no records exist."
    )
    public ResponseEntity<ReceiptStatusCountDto> getApproverReceiptCounts(
            @AuthenticationPrincipal RexUserContext rexUserContext) {

        Long rexUserId = rexUserContext.getRexUserId();

        return ResponseEntity.ok(receiptCountService.getApproverReceiptCounts(rexUserId));
    }

    @GetMapping("/submitter")
    @Operation(
            summary = "Get submitter's receipts list",
            description = "Fetches a paginated list of receipts with optional filters such as status, title, submitter, date range, category, and amount. Supports sorting and pagination."
    )
    public ResponseEntity<ReceiptGetListSubmitterResponseDto> getSubmitterReceipts(

            @Parameter(description = "Filter by status (e.g. SUBMITTED, APPROVED, REJECTED, CORRECTION_REQUESTED)")
            @RequestParam(required = false) ReceiptStatusEnum status,

            @Parameter(description = "Filter by receipt title (partial match)")
            @RequestParam(name = "receipt_title", required = false) String receiptTitle,

            @Parameter(description = "Filter by submitter name (partial match)")
            @RequestParam(name = "submitted_by", required = false) String submittedBy,

            @Parameter(description = "Filter from date (YYYY-MM-DD)")
            @RequestParam(name = "receipt_from_date", required = false) LocalDate receiptFromDate,

            @Parameter(description = "Filter to date (YYYY-MM-DD)")
            @RequestParam(name = "receipt_to_date", required = false) LocalDate receiptToDate,

            @Parameter(description = "Filter by category (partial match)")
            @RequestParam(required = false) String category,

            @Parameter(description = "Filter by exact receipt amount")
            @RequestParam(name = "receipt_amount", required = false) BigDecimal receiptAmount,

            @Parameter(description = "Page number (starts from 1, default: 1)")
            @RequestParam(name = "page_no", defaultValue = "1") int pageNo,

            @Parameter(description = "Page size (default: 20)")
            @RequestParam(name = "page_size", defaultValue = "20") int pageSize,

            @Parameter(description = "Sort by field")
            @RequestParam(name = "sort_by", required = false) ReceiptSortSubmittedBy sortBy,

            @Parameter(description = "Sort order (ASC or DESC)")
            @RequestParam(name = "sort_order", required = false) String sortOrder,

            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        if ((receiptFromDate == null) != (receiptToDate == null)) {
            throw new BadRequestException(
                    "Invalid Request: Please provide both receipt_from_date & receipt_to_date, or neither"
            );
        }

        Long rexUserId = rexUserContext.getRexUserId();

        ReceiptGetListSubmitterResponseDto response = receiptsService.getSubmitterReceipts(
                rexUserId,
                status,
                receiptTitle,
                submittedBy,
                receiptFromDate,
                receiptToDate,
                category,
                receiptAmount,
                pageNo,
                pageSize,
                sortBy,
                sortOrder
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/approver")
    @Operation(
            summary = "Get approver's receipts list",
            description = "Fetches a paginated list of receipts with optional filters such as status, title, submitter, date range, category, and amount. Supports sorting and pagination."
    )
    public ResponseEntity<ReceiptGetListApproverResponseDto> getApproverReceipts(

            @Parameter(description = "Filter by status (e.g. SUBMITTED, APPROVED, REJECTED, CORRECTION_REQUESTED)")
            @RequestParam(required = false) ReceiptStatusEnum status,

            @Parameter(description = "Filter by receipt title (partial match)")
            @RequestParam(name = "receipt_title", required = false) String receiptTitle,

            @Parameter(description = "Filter by submitter name (partial match)")
            @RequestParam(name = "submitted_for", required = false) String submittedFor,

            @Parameter(description = "Filter from date (YYYY-MM-DD)")
            @RequestParam(name = "receipt_from_date", required = false) LocalDate receiptFromDate,

            @Parameter(description = "Filter to date (YYYY-MM-DD)")
            @RequestParam(name = "receipt_to_date", required = false) LocalDate receiptToDate,

            @Parameter(description = "Filter by category (partial match)")
            @RequestParam(required = false) String category,

            @Parameter(description = "Filter by exact receipt amount")
            @RequestParam(name = "receipt_amount", required = false) BigDecimal receiptAmount,

            @Parameter(description = "Page number (starts from 1, default: 1)")
            @RequestParam(name = "page_no", defaultValue = "1") int pageNo,

            @Parameter(description = "Page size (default: 20)")
            @RequestParam(name = "page_size", defaultValue = "20") int pageSize,

            @Parameter(description = "Sort by field")
            @RequestParam(name = "sort_by", required = false) ReceiptSortApproverBy sortBy,

            @Parameter(description = "Sort order (ASC or DESC)")
            @RequestParam(name = "sort_order", required = false) String sortOrder,

            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {

        if ((receiptFromDate == null) != (receiptToDate == null)) {
            throw new BadRequestException(
                    "Invalid Request: Please provide both receipt_from_date & receipt_to_date, or neither"
            );
        }

        Long rexUserId = rexUserContext.getRexUserId();

        ReceiptGetListApproverResponseDto response = receiptsService.getApproverReceipts(
                rexUserId,
                status,
                receiptTitle,
                submittedFor,
                receiptFromDate,
                receiptToDate,
                category,
                receiptAmount,
                pageNo,
                pageSize,
                sortBy,
                sortOrder
        );

        return ResponseEntity.ok(response);
    }

    
}
