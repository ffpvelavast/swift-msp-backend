/** REST controller exposing Taxation API endpoints; all queries are scoped to the authenticated tenant via RexUserContext. */
package com.rex.commodis.controller;

import com.rex.commodis.dto.receipts.ExpenseReasonDropDownDto;
import com.rex.commodis.security.user.RexUserContext;
import com.rex.commodis.service.RexUserService;
import com.rex.commodis.service.TaxationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taxation")
@RequiredArgsConstructor
@Tag(name = "Taxation", description = "APIs for the taxation related dropdowns for create receipt screen")
public class TaxationController {

    private final TaxationService taxationService;
    private final RexUserService rexUserService;

    @GetMapping("/{taxCategoryId}/reasons-dropdown")
    @Operation(
            summary = "Get tax reasons for selected category",
            description = "Returns a list of tax reasons associated with a specific tax category. " +
                    "This endpoint is used to populate the second (dependent) dropdown in the UI. " +
                    "The taxCategoryId must correspond to a valid category selected by the user."
    )
    public List<ExpenseReasonDropDownDto> getTaxReasons(
            @PathVariable Long taxCategoryId,
            @AuthenticationPrincipal RexUserContext rexUserContext
    ) {
        Long platformId = rexUserContext.getPlatformId();
        Long rexUserId = rexUserContext.getRexUserId();

        String Locale = rexUserService.getLocale(rexUserId);

        return taxationService.getTaxReasons(platformId, taxCategoryId, Locale);
    }
}
