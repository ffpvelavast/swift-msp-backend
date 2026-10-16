/** JPA entity representing the PicklistMemberRow table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
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
public class PicklistMemberRow {
    private String memberType;
    private Long memberId;
    private String listname;
    private String organization;
    private String identityTag;
    private String orgunit;
    private String costcenter;
}
