/** JPA entity representing the CorporateEventAdminId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.events.CorporateEventAdmin;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CorporateEventAdminId implements Serializable {

    private Long eventId;

    private Long rexUserId;
}
