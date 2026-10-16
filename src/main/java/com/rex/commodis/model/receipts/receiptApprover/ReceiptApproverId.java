/** JPA entity representing the ReceiptApproverId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts.receiptApprover;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReceiptApproverId implements Serializable {

    private Long receiptId;

    private Long rexUserId;
}