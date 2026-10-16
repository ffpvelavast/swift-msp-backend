/** JPA entity representing the BeneficiaryOrgRow table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryOrgRow {
    private Long beneficiaryId;
    private String listname;
    private String fullname;
    private String identity_tag;
    private String employer;
    private String orgunit;
    private String costcenter;
    private Boolean isFavorite;
}
