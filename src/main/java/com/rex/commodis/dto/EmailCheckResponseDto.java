/** Response DTO returned by the EmailCheck API endpoint. */
package com.rex.commodis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCheckResponseDto {

    @JsonProperty("exists")
    @Schema(
            description = "Whether the email exists in the system",
            example = "true"
    )
    private Boolean exists;
}