/** DTO carrying InvoicePdfLinkResponse data for the invoice listing, details, or delegation flow. */
package com.rex.commodis.dto.invoiceDtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoicePdfLinkResponseDto {
	
	private String invoicePdfLink;

}