/** JPA entity representing the GuestRow table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */

package com.rex.commodis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestRow {
    private Long guestId;
    private Boolean organisationwideGuest;
    private String listname;
    private String organization;
    private String guestEnum;
    private String guestType;
}

