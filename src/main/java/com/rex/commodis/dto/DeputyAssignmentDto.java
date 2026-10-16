/** Data transfer object carrying DeputyAssignment data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeputyAssignmentDto {
    private Long id;
    private LocalDate activeFrom;
    private LocalDate activeThrough;
    private Long rexUserId;
    private Long deputyId;
    private Instant createdAt;
    private Instant updatedAt;
}
