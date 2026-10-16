/** Data transfer object carrying DeputyRightAssignment data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeputyRightAssignmentDto {
    private Long deputyAssignmentId;
    private Long deputyRightId;
}
