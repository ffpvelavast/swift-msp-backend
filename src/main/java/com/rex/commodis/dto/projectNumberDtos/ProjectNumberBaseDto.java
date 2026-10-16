/** Data transfer object carrying ProjectNumberBase data between the API layer and the service/client. */
package com.rex.commodis.dto.projectNumberDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectNumberBaseDto {

    private UUID projectNumberId;
    private String projectNumber;
    private String projectTitle;
    private String projectSubtitle;
    private String description;
    private LocalDate activeFrom;
    private LocalDate activeThrough;
    private List<ProjectLeaderSummaryDto> projectLeaders;
}
