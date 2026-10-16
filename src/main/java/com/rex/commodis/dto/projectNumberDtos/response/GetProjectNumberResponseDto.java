/** Response DTO returned by the GetProjectNumber API endpoint. */
package com.rex.commodis.dto.projectNumberDtos.response;

import com.rex.commodis.dto.projectNumberDtos.ProjectNumberBaseDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetProjectNumberResponseDto extends ProjectNumberBaseDto {
    public GetProjectNumberResponseDto(ProjectNumberBaseDto base) {
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
    }
}
