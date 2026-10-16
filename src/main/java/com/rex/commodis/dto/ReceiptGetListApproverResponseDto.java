/** Response DTO returned by the ReceiptGetListApprover API endpoint. */
package com.rex.commodis.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReceiptGetListApproverResponseDto {

    private ReceiptGetListApproverMetaDto meta;
    private List<ReceiptGetListApproverDto> data;
}