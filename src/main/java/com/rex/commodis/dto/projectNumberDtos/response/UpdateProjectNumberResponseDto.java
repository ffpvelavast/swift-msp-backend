/** Response DTO returned by the UpdateProjectNumber API endpoint. */
package com.rex.commodis.dto.projectNumberDtos.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.rex.commodis.dto.projectNumberDtos.ProjectNumberBaseDto;

@Data
@NoArgsConstructor
public class UpdateProjectNumberResponseDto extends ProjectNumberBaseDto {

    private List<String> invalidEmails;
    public UpdateProjectNumberResponseDto(ProjectNumberBaseDto projectNumberBaseDto, List<String> invalidEmails) {
        super(
        		projectNumberBaseDto.getProjectNumberId(),
        		projectNumberBaseDto.getProjectNumber(),
        		projectNumberBaseDto.getProjectTitle(),
        		projectNumberBaseDto.getProjectSubtitle(),
        		projectNumberBaseDto.getDescription(),
        		projectNumberBaseDto.getActiveFrom(),
        		projectNumberBaseDto.getActiveThrough(),
        		projectNumberBaseDto.getProjectLeaders()
        );
        this.invalidEmails = invalidEmails;
    }
}
