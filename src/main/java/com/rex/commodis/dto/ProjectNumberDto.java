/** Data transfer object carrying ProjectNumber data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectNumberDto {

    private Long id;
    private Long platformId;
    private UUID projectNumberId;
    private String externalProjectId;
    private String projectNumber;
    private String projectTitle;
    private String projectSubtitle;
    private String description;
    private LocalDate activeFrom;
    private LocalDate activeThrough;
    private List<ProjectLeaderDto> projectLeaders;

}
