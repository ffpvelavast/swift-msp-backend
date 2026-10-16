/** Request DTO for the ProjectNumberSearch operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectNumberSearchRequestDto {
    private Long platformId;
    private String query;
    private Integer limit;

}
