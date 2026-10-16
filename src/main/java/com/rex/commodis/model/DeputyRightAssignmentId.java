/** JPA entity representing the DeputyRightAssignmentId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeputyRightAssignmentId implements Serializable {
    private Long deputyAssignmentId;
    private Long deputyRightId;
}
