/** Data transfer object carrying ProjectLeaderSummary data between the API layer and the service/client. */
package com.rex.commodis.dto.projectNumberDtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectLeaderSummaryDto {

	@NotNull
    private String email;
	
	@NotNull
    private LocalDate activeFrom;
    
    private LocalDate activeThrough;
}
