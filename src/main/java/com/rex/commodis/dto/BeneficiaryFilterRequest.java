/** Request DTO for the BeneficiaryFilter operation; validated and passed from the controller to the service layer. */
package com.rex.commodis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryFilterRequest {
    private java.util.UUID beneficiaryUuid;
    private String lastName;
    private String firstName;
    private String academicTitle;
    private String listName;

    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
}
