/** Data transfer object carrying AssignedBeneficiary data between the API layer and the service/client. */
package com.rex.commodis.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AssignedBeneficiaryDto {
    private Long id;
    private UUID beneficiaryId;
    private UUID commodisId;
    private String firstName;
    private String lastName;
    private String academicTitle;
    private String listName;
    private String gender;
    private String emailAddress;
    private LocalDate leavingDate;
    private String externalSystemId;
    private Instant createdAt;
    private Instant updatedAt;
}