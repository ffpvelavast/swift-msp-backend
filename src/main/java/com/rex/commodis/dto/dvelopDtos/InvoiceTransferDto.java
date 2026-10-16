/** DTO representing the InvoiceTransferDto data structure received from the D-velop document management webhook. */
package com.rex.commodis.dto.dvelopDtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceTransferDto {

    private InvoiceWorkflowDto workflow;

    @JsonProperty("_links")
    private InvoiceTransferLinksDto links;
}
