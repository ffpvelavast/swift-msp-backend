/** Response DTO returned by the ProjectNumberSearch API endpoint. */
package com.rex.commodis.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectNumberSearchResponseDto {
    private Long projectNumberId;
    private String projectNumber;
    private String projectTitle;
    private String projectSubtitle;
}
