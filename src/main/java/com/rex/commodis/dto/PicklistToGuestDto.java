/** Data transfer object carrying PicklistToGuest data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PicklistToGuestDto {
    private Long picklistId;
    private Long guestId;
    private Instant createdAt;
}
