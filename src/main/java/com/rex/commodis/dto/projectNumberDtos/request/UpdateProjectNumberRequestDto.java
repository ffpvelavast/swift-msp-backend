/** Request DTO for the UpdateProjectNumber operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto.projectNumberDtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.rex.commodis.dto.projectNumberDtos.ProjectLeaderSummaryDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectNumberRequestDto {

    private String projectNumber;

    //Optional fields
    private String projectTitle;
    private String projectSubtitle;
    private String description;

    private LocalDate activeFrom;
    private LocalDate activeThrough;

    private List<ProjectLeaderSummaryDto> projectLeaders;

}
