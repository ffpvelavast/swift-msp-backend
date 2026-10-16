/** REST controller exposing Invoices API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import java.time.LocalDate;
import java.util.List;

import com.rex.commodis.dto.invoiceDtos.*;
import com.rex.commodis.dto.receipts.BookableEventDto;
import com.rex.commodis.dto.receipts.ExpenseCategoryDropDownDto;
import com.rex.commodis.service.RexUserService;
import com.rex.commodis.service.TaxationService;
import com.rex.commodis.service.TeamEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rex.commodis.dto.*;
import com.rex.commodis.exceptions.BadRequestException;
import com.rex.commodis.model.enums.InvoiceSortBy;
import com.rex.commodis.model.enums.InvoiceStatusRequestEnum;
import com.rex.commodis.security.user.RexUserContext;
import com.rex.commodis.service.invoiceServices.InvoicePositionService;
import com.rex.commodis.service.invoiceServices.InvoicesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoicesController {

	private final InvoicesService invoicesService;
	private final InvoicePositionService invoicePositionService;
	private final TeamEventService teamEventService;
	private final TaxationService taxationService;
	private final RexUserService rexUserService;

	@PostMapping("/save-invoice")
	public ResponseEntity<InvoiceSaveDraftResponseDto> saveDraft(@RequestBody InvoiceSaveDraftRequestDto invoiceSaveDraftRequestDto,
                                                                 @AuthenticationPrincipal RexUserContext rexUserContext) {

		// RexUser lookup
		Long rexUserId = rexUserContext.getRexUserId();

		// Call service
		InvoiceSaveDraftResponseDto invoiceSaveDraftResponseDto =
				invoicesService.processSaveDraft(rexUserId, invoiceSaveDraftRequestDto);

		return ResponseEntity.ok(invoiceSaveDraftResponseDto);
	}


//	@GetMapping
//	public ResponseEntity<InvoiceGetListResponseDto> getInvoices(
//			@Parameter(
//					description = "Filter by status",
//					schema = @Schema(implementation = InvoiceStatusRequestEnum.class)
//			)
//			@RequestParam(required = false) String status,
//
//			@Parameter(description = "Filter by vendor name (supports partial match)")
//			@RequestParam(required = false) String vendor,
//
//			@Parameter(description = "From date (format: YYYY-MM-DD)", schema = @Schema(type = "string", format = "date"))
//			@RequestParam(name = "from_date", required = false) LocalDate fromDate,
//
//			@Parameter(description = "To date (format: YYYY-MM-DD)", schema = @Schema(type = "string", format = "date"))
//			@RequestParam(name = "to_date", required = false) LocalDate toDate,
//
//			@Parameter(description = "Page number (starts from 1)", schema = @Schema(type = "integer", defaultValue = "1"))
//			@RequestParam(name = "page_no", defaultValue = "1") int pageNo,
//
//			@Parameter(description = "Page size", schema = @Schema(type = "integer", defaultValue = "20"))
//			@RequestParam(name = "page_size", defaultValue = "20") int pageSize,
//
//			@Parameter(description = "Sort by field", schema = @Schema(implementation = InvoiceSortBy.class))
//			@RequestParam(name = "sort_by", required = false) InvoiceSortBy sortBy,
//
//			@Parameter(description = "Sort order", schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}))
//			@RequestParam(name = "sortOrder", required = false) String sortOrder,
//
//			@AuthenticationPrincipal RexUserContext rexUserContext
//	) {
//
//		if ((fromDate == null) != (toDate == null)) {
//			throw new BadRequestException("Invalid Request: Please provide both from & to date, or neither");
//		}
//
//		Long rexUserId = rexUserContext.getRexUserId();
//
//		InvoiceGetListResponseDto response = invoicesService.getInvoices(
//				rexUserId,
//				status,
//				vendor,
//				fromDate,
//				toDate,
//				pageNo,
//				pageSize,
//				sortBy,
//				sortOrder
//		);
//
//		return ResponseEntity.ok(response);
//	}
//
//	@PostMapping("/{invoiceId}/status")
//	@Operation(
//			summary = "Update invoice status",
//			description = "Status must be one of: DRAFT, PENDING, RELEASED, REJECTED"
//	)
//	public ResponseEntity<Void> updateInvoiceStatus(
//			@PathVariable Long invoiceId,
//			@RequestBody InvoiceStatusUpdateRequestDto requestDto,
//			@AuthenticationPrincipal RexUserContext rexUserContext
//			) {
//
//		Long rexUserId = rexUserContext.getRexUserId();
//		invoicesService.updateInvoiceStatus(invoiceId, requestDto.getStatus(), rexUserId);
//		return ResponseEntity.ok().build();
//	}
//
//	@GetMapping("/{invoiceId}/invoice-details")
//	public ResponseEntity<InvoiceDetailsResponseDto> getInvoiceDetails(
//			@PathVariable Long invoiceId,
//			@AuthenticationPrincipal RexUserContext rexUserContext
//			) {
//
//		// Hardcoded for now
//		Long rexUserId = rexUserContext.getRexUserId();
//
//		InvoiceDetailsResponseDto response =
//				invoicesService.getInvoiceDetails(invoiceId, rexUserId);
//
//		return ResponseEntity.ok(response);
//	}
//
//	@GetMapping("/delegated")
//	public ResponseEntity<InvoiceDelegateDetailsResponseDto> getDelegatedInvoices(
//			@RequestParam(name = "from", required = false)LocalDate from,
//			@RequestParam(name = "to", required = false) LocalDate to,
//			@RequestParam(name = "Page_no", defaultValue = "1") int page,
//			@RequestParam(name = "page_size", defaultValue = "20") int pageSize,
//			@AuthenticationPrincipal RexUserContext rexUserContext
//	) {
//
//		if ((from == null) != (to == null)) {
//			throw new BadRequestException("Date range data missing.");
//		}
//		// Get rexUserId from request (NO hardcoding)
//		// Long rexUserId = (Long) request.getAttribute("rexUserId");
//		Long rexUserId = rexUserContext.getRexUserId(); // temporary
//
//		InvoiceDelegateDetailsResponseDto response =
//				invoicesService.getDelegatedInvoices(
//				rexUserId,
//				from,
//				to,
//				page,
//				pageSize
//		);
//
//		return ResponseEntity.ok(response);
//	}
//
//
//	@PostMapping("/{invoiceId}/responsibilities")
//	public ResponseEntity<InvoiceDelegateResponseDto> delegateInvoice(
//			@PathVariable Long invoiceId,
//			@Valid @RequestBody InvoiceDelegateRequestDto requestDto,
//			@AuthenticationPrincipal RexUserContext rexUserContext
//	) {
//
//		Long rexUserId = rexUserContext.getRexUserId();
//
//		InvoiceDelegateResponseDto response =
//				invoicesService.delegateInvoice(invoiceId, requestDto, rexUserId);
//
//		return ResponseEntity.ok(response);
//	}
//
//	@GetMapping("/{invoiceId}/beneficiaries")
//	public ResponseEntity<List<AssignedBeneficiaryDto>> getAssignedBeneficiaries(
//			@PathVariable Long invoiceId,
//			@AuthenticationPrincipal RexUserContext rexUserContext
//	) {
//
//		Long rexUserId = rexUserContext.getRexUserId();
//
//		return ResponseEntity.ok(
//				invoicesService.getAssignedBeneficiaries(invoiceId, rexUserId)
//		);
//	}
//
//	@GetMapping("/{invoiceId}/pdf-link")
//	public ResponseEntity<InvoicePdfLinkResponseDto> getInvoicePdfLink(
//			@PathVariable Long invoiceId,
//			@AuthenticationPrincipal RexUserContext rexUserContext){
//
//		Long rexUserId = rexUserContext.getRexUserId();
//		Long platformId = rexUserContext.getPlatformId();
//
//		InvoicePdfLinkResponseDto response =
//				invoicesService.getInvoicePdfLink(invoiceId, rexUserId, platformId);
//
//		return ResponseEntity.ok(response);
//	}
//
//	@GetMapping("/events")
//	public ResponseEntity<List<BookableEventDto>> getEvents(@AuthenticationPrincipal RexUserContext rexUserContext) {
//
//		Long rexUserId  = rexUserContext.getRexUserId();
//		List<BookableEventDto> response = teamEventService.getBookableEvents(rexUserId);
//
//		return ResponseEntity.ok(response);
//	}

	@PostMapping("/{invoiceId}/save-positions")
	@Operation(
			summary = "Save invoice position data",
			description = "Updates taxReasonId, beneficiaries, and guests for each invoice position. " +
					"Null fields are treated as no-op. An empty list clears all existing entries."
	)
	public ResponseEntity<SavePositionsResponseDto> savePositions(
			@PathVariable Long invoiceId,
			@RequestBody SavePositionsRequestDto requestDto,
			@AuthenticationPrincipal RexUserContext rexUserContext
	) {
		Long rexUserId = rexUserContext.getRexUserId();
		SavePositionsResponseDto response = invoicePositionService.savePositions(invoiceId, rexUserId, requestDto);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/category-dropdown")
	@Operation(
			summary = "Get tax categories dropdown",
			description = "Returns a list of tax categories for the authenticated user's platform and locale. " +
					"Used to populate the category dropdown in the invoice screen."
	)
	public List<ExpenseCategoryDropDownDto> getTaxCategories(
			@AuthenticationPrincipal RexUserContext rexUserContext
	) {
		Long platformId = rexUserContext.getPlatformId();
		Long rexUserId = rexUserContext.getRexUserId();
		String locale = rexUserService.getLocale(rexUserId);

		return taxationService.getTaxCategories(platformId, locale);
	}

}
