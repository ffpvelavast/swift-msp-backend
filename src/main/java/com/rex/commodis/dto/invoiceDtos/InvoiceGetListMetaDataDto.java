/** DTO carrying InvoiceGetListMetaData data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class InvoiceGetListMetaDataDto {
    private Long rexUserId;
    private LocalDate from;
    private LocalDate to;
    private String status;
    private int page;
    @JsonProperty("page_size")
    private int pageSize;
    private String sortBy;
    private String direction;
}