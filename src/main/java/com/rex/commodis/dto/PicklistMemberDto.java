/** Data transfer object carrying PicklistMember data between the API layer and the service/client. */
package com.rex.commodis.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PicklistMemberDto {
    private String memberType; // "BENEFICIARY" or "GUEST"
    private Long memberId;
    private String listname;      // Matches SQL: listname
    private String organization;  // Matches SQL: organization
    private String identityTag;   // Matches SQL: identity_tag
    private String orgUnit;       // Matches SQL: orgunit
    private String costCenter;    // Matches SQL: costcenter
    private Boolean isFavorite;
    private Long picklistId;
}

