package com.rex.commodis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response payload after successfully creating a picklist")
public class PicklistCreateResponseDto {

    @JsonProperty("picklist_id")
    @Schema(description = "Generated ID of the newly created picklist", example = "42")
    private Long picklistId;
}
