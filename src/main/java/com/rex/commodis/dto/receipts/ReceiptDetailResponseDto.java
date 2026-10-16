/** Response DTO returned by the ReceiptDetail API endpoint. */
package com.rex.commodis.dto.receipts;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rex.commodis.dto.BeneficiaryNamesDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDetailResponseDto {

    @JsonProperty("receipt_id")
    private Long receiptId;

    @JsonProperty("receipt_title")
    private String receiptTitle;

    @JsonProperty("receipt_date")
    private LocalDate receiptDate;

    @JsonProperty("expense_category")
    private String expenseCategory;

    @JsonProperty("expense_nature")
    private String expenseNature;

    @JsonProperty("status")
    private String status;

    @JsonProperty("location")
    private String location;

    @JsonProperty("team_balance_deduct")
    private Boolean teamBalanceDeduct;

    @JsonProperty("receipt_amount")
    private String receiptAmount;

    @JsonProperty("submitted_for")
    private String submittedFor;

    @JsonProperty("receipt_To_Beneficiaries")
    private BeneficiaryNamesDto receiptToBeneficiaries;

    @JsonProperty("receipt_To_Guests")
    private BeneficiaryNamesDto receiptToGuests;

    @JsonProperty("internal_note")
    private String internalNote;

    @JsonProperty("external_note")
    private String externalNote;

    @JsonProperty("files")
    private List<ReceiptFileResponseDto> files;
}
