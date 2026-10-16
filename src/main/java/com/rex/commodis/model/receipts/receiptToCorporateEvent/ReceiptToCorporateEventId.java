/** JPA entity representing the ReceiptToCorporateEventId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts.receiptToCorporateEvent;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReceiptToCorporateEventId implements Serializable {

    private Long receiptId;

    private Long corporateEventId;
}
