/** Response DTO returned by the GetPlatformByEmail API endpoint. */
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
public class GetPlatformByEmailResponseDto {

    @JsonProperty("platformId")
    @Schema(
            description = "The platform ID associated with the email",
            example = "123"
    )
    private Long platformId;

    @JsonProperty("displayName")
    @Schema(
            description = "The display name of the platform",
            example = "Example Platform"
    )
    private String displayName;
}