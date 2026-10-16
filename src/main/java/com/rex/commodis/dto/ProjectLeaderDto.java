/** Data transfer object carrying ProjectLeader data between the API layer and the service/client. */
package com.rex.commodis.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProjectLeaderDto {
    private Long id;
    private Long projectId;
    private Long rexUserId;
    private String email;
    private LocalDate activeFrom;
    private LocalDate activeThrough;
}
