/** Response DTO returned by the CreateProjectNumber API endpoint. */
package com.rex.commodis.dto.projectNumberDtos.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.rex.commodis.dto.projectNumberDtos.ProjectNumberBaseDto;

@Data
@NoArgsConstructor
public class CreateProjectNumberResponseDto extends ProjectNumberBaseDto {

    private List<String> invalidEmails;

    public CreateProjectNumberResponseDto(ProjectNumberBaseDto base, List<String> invalidEmails) {
        super(
                base.getProjectNumberId(),
                base.getProjectNumber(),
                base.getProjectTitle(),
                base.getProjectSubtitle(),
                base.getDescription(),
                base.getActiveFrom(),
                base.getActiveThrough(),
                base.getProjectLeaders()
        );
        this.invalidEmails = invalidEmails;
    }
}
