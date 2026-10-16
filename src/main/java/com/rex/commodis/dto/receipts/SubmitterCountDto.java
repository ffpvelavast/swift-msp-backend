package com.rex.commodis.dto.receipts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubmitterCountDto {

    @JsonProperty("submitter_receipts_count")
    private Long submitterReceiptsCount;
}