/** DTO representing the InvoiceTransferLinksDto data structure received from the D-velop document management webhook. */
package com.rex.commodis.dto.dvelopDtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceTransferLinksDto {

    @JsonProperty("dmsobject")
    private InvoiceTransferHrefDto dmsObject;

    @JsonProperty("report_results_async")
    private InvoiceTransferHrefDto reportResultsAsync;
}
