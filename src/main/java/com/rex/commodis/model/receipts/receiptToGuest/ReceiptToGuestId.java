/** JPA entity representing the ReceiptToGuestId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.receipts.receiptToGuest;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReceiptToGuestId implements Serializable {

    private Long receiptId;

    private Long guestId;
}