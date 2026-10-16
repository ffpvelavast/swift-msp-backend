/** DTO representing the InvoiceTransferCurrencyDto data structure received from the D-velop document management webhook. */
package com.rex.commodis.dto.dvelopDtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceTransferCurrencyDto {

    private String code;
}
