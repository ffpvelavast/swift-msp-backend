/** Data transfer object carrying Employer data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployerDto {
    private Long id;
    private java.util.UUID employerUuid;
    private Long platformId;
    private String displayName;
    private java.time.LocalDate activeFrom;
    private java.time.LocalDate activeThrough;
    private Instant createdAt;
    private Instant updatedAt;
}
