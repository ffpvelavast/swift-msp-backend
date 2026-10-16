/** Data transfer object carrying ReceiptStatusCount data between the API layer and the service/client. */
package com.rex.commodis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rex.commodis.model.enums.ReceiptStatusEnum;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ReceiptStatusCountDto {

    private long total;

    private Map<ReceiptStatusEnum, Long> byStatus;
}