/** Data transfer object carrying TeamEventReceipt data between the API layer and the service/client. */
package com.rex.commodis.dto.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rex.commodis.dto.receipts.ReceiptFileResponseDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamEventReceiptDto {

    @JsonProperty("receipt_id")
    private Long receiptId;

    @JsonProperty("receiptTitle")
    private String receiptTitle;

    @JsonProperty("receiptDate")
    private LocalDate receiptDate;

    @JsonProperty("receiptNumber")
    private String receiptNumber;

    @JsonProperty("submitted_for")
    private String submittedFor;

    @JsonProperty("receiptAmount")
    private BigDecimal receiptAmount;

    @JsonProperty("receiptCurrency")
    private String receiptCurrency;

    @JsonProperty("gratuity")
    private BigDecimal gratuity;

    @JsonProperty("amountNetFullVat")
    private BigDecimal amountNetFullVat;

    @JsonProperty("vatRateFull")
    private Short vatRateFull;

    @JsonProperty("expense_category")
    private String expenseCategory;

    @JsonProperty("expense_nature")
    private String expenseNature;

    @JsonProperty("issuanceRegion")
    private String issuanceRegion;

    @JsonProperty("deductTeamBalance")
    private Boolean deductTeamBalance;

    @JsonProperty("receipt_files")
    private List<ReceiptFileResponseDto> receiptFiles;

    private String status;
}