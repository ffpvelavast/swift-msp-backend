/** Data transfer object carrying ReceiptGetListSubmitterMeta data between the API layer and the service/client. */
package com.rex.commodis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ReceiptGetListSubmitterMetaDto {

    private Long rexUserId;

    private String status;

    @JsonProperty("receipt_title")
    private String receiptTitle;

    @JsonProperty("submitted_by")
    private String submittedBy;

    @JsonProperty("receipt_from_date")
    private LocalDate receiptFromDate;

    @JsonProperty("receipt_to_date")
    private LocalDate receiptToDate;

    private String category;

    @JsonProperty("receipt_amount")
    private BigDecimal receiptAmount;

    private int page;

    @JsonProperty("page_size")
    private int pageSize;

    private String sortBy;

    private String direction;
}