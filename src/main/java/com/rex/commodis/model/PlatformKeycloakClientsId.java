/** JPA entity representing the PlatformKeycloakClientsId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformKeycloakClientsId implements Serializable {
    private Long id;
    private LocalDate activeFrom;
}
