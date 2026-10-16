/** Request DTO for the CreateReceipt operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto.receipts;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReceiptRequestDto {

    @NotNull(message = "Expense report ID is required")
    private Long expenseReportId;

    @NotNull(message = "Issuance region ID is required")
    private Short issuanceRegionId;

    @NotBlank(message = "Receipt title is required")
    private String receiptTitle;

    @NotNull(message = "Receipt date is required")
    private LocalDate receiptDate;

    @NotNull(message = "Receipt amount is required")
    @DecimalMin(value = "0.0")
    private BigDecimal receiptAmount;

    @NotBlank(message = "Receipt currency is required")
    private String receiptCurrency;

    @NotNull(message = "Project number ID is required")
    private Long projectNumberId;

    @NotNull(message = "Tax reason client ID is required")
    private Long taxReasonClientId;

    private BigDecimal gratuity;

    private BigDecimal amountNetReducedVat;

    private BigDecimal amountNetFullVat;

    private Short vatRateReduced;

    private Short vatRateFull;

    private Boolean deductTeamBalance;

    private List<Long> beneficiaryIds;

    private List<Long> guestIds;

    private Long receiptFileTypeId;

    private ReceiptEventRequestDto receiptEvent;
}