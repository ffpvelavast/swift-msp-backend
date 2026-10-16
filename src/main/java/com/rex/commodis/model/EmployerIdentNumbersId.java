/** JPA entity representing the EmployerIdentNumbersId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import lombok.*;
import java.io.Serializable;

/**
 * Composite primary key for EmployerIdentNumbers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployerIdentNumbersId implements Serializable {
    private Long employerId;
    private String companyIdPayroll;
}
