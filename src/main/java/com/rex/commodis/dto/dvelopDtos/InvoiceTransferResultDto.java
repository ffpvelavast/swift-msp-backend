/** DTO representing the InvoiceTransferResultDto data structure received from the D-velop document management webhook. */
package com.rex.commodis.dto.dvelopDtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceTransferResultDto {

    private boolean successful;

    private InvoiceTransferResultErrorDto error;
}
