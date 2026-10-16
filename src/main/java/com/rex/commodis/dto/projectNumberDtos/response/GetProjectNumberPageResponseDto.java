/** Response DTO returned by the GetProjectNumberPage API endpoint. */
package com.rex.commodis.dto.projectNumberDtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.rex.commodis.dto.projectNumberDtos.ProjectNumberBaseDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProjectNumberPageResponseDto {
    private List<ProjectNumberBaseDto> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
