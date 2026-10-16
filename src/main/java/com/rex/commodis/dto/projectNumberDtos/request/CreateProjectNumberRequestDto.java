/** Request DTO for the CreateProjectNumber operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto.projectNumberDtos.request;

import java.time.LocalDate;
import java.util.List;

import com.rex.commodis.dto.projectNumberDtos.ProjectLeaderSummaryDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectNumberRequestDto {

    @NotBlank
    private String projectNumber;

    @NotBlank
    private String projectTitle;

    private String projectSubtitle;
    private String description;

    @NotNull
    private LocalDate activeFrom;

    private LocalDate activeThrough;

    @Valid
    private List<ProjectLeaderSummaryDto> projectLeaders;

}
