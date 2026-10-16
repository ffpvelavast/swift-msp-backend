/** JPA entity representing the ReceiptToBeneficiaryId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts.receiptToBeneficiary;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReceiptToBeneficiaryId implements Serializable {

    private Long receiptId;

    private Long beneficiaryId;
}