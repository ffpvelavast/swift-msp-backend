/** Response DTO returned by the ReceiptGetListSubmitter API endpoint. */
package com.rex.commodis.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReceiptGetListSubmitterResponseDto {

    private ReceiptGetListSubmitterMetaDto meta;
    private List<ReceiptGetListSubmitterDto> data;
}