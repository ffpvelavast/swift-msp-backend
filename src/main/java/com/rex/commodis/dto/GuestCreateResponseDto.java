package com.rex.commodis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response DTO returned after successfully creating a guest.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response payload after successfully creating a guest")
public class GuestCreateResponseDto {

    @JsonProperty("guest_id")
    @Schema(description = "Generated ID of the newly created guest", example = "105")
    private Long guestId;
}
