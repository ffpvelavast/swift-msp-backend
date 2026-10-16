package com.rex.commodis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response payload containing the details of a guest")
public class GuestResponseDto {

    @Schema(description = "Unique database ID of the guest", example = "105")
    private Long id;

    @Schema(description = "Whether the guest is visible across the entire organization", example = "true")
    private Boolean organisationWide;

    @Schema(description = "Formatted display name of the guest (e.g. 'Lastname, Firstname')", example = "Doe, John")
    private String listname;

    @Schema(description = "Organization or company the guest belongs to", example = "Acme Corp")
    private String organization;

    @Schema(description = "Internal uppercase enum code representing the guest type", example = "CUSTOMER")
    private String guestEnum;

    @Schema(description = "Localized representation of the guest type", example = "Client")
    private String guestType;
}