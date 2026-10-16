/** JPA entity representing the PlatformConfigurationId table; mapped to the rexbase/invoices/receipts schema and scoped by platformId. */
package com.rex.commodis.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PlatformConfigurationId implements Serializable {

private Long id;
private LocalDate activeFrom;

}
