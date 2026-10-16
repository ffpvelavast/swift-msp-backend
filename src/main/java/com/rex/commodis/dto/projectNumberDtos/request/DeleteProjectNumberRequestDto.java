/** Request DTO for the DeleteProjectNumber operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto.projectNumberDtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProjectNumberRequestDto {
    private String projectNumber;
}
