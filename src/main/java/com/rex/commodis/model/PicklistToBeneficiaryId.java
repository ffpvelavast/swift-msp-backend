/** JPA entity representing the PicklistToBeneficiaryId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PicklistToBeneficiaryId implements Serializable {
    private Long picklistId;
    private Long beneficiaryId;
}
