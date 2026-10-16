/** JPA entity representing the ReceiptToTeamEventId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts.receiptToTeamEvent;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReceiptToTeamEventId implements Serializable {

    private Long receiptId;

    private Long teamEventId;
}
