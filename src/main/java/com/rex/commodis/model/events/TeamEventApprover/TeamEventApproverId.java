/** JPA entity representing the TeamEventApproverId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model.events.TeamEventApprover;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TeamEventApproverId implements Serializable {

    private Long teamEventId;

    private Long rexUserId;
}