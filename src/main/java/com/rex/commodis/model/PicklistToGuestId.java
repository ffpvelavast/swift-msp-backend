/** JPA entity representing the PicklistToGuestId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PicklistToGuestId implements Serializable {
    private Long picklistId;
    private Long guestId;
}
