/** Data transfer object carrying Beneficiary data between the API layer and the service/client. */


// BeneficiaryDto.java
package com.rex.commodis.dto;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryDto {
    private Long id;
    private java.util.UUID beneficiaryId;
    private java.util.UUID commodisId;
    private String firstName;
    private String lastName;
    private String academicTitle;
    private String listName;
    private String fullName;
    private String gender;
    private String emailAddress;
    private LocalDate leavingDate;
    private String externalSystemId;
    private String identityTag;     // New: from select_beneficiary
    private String orgUnit;         // New: from select_beneficiary
    private String costCenter;      // New: from select_beneficiary
    private String employerName;    // New: from select_beneficiary
    private Instant createdAt;
    private Instant updatedAt;
}

