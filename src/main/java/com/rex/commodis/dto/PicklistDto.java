/** Data transfer object carrying Picklist data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for listing picklists in the UI (e.g. sidebar).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PicklistDto {
    private Long id;
    private String name;
    private Short sortId;
    private Instant createdAt;
    private Long memberCount;
}
