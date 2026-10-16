/** Data transfer object carrying BeneficiaryToEmployer data between the API layer and the service/client. */
package com.rex.commodis.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryToEmployerDto {
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Beneficiary ID cannot be null")
    private Long beneficiaryId;

    @NotNull(message = "Employer ID cannot be null")
    private Long employerId;

    @NotNull(message = "Valid from date cannot be null")
    private LocalDate validFrom;

    private LocalDate validThrough;
    private String personnelNumberSystem;
    private String personnelNumberPayroll;
    private String identityTag;
    private String companySite;
    private String orgUnit;
    private String costCenter;

    @NotNull(message = "Created date cannot be null")
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    // optional nested
    private BeneficiaryDto beneficiary;
    private EmployerDto employer;
}
