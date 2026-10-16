/** Spring Data JPA repository for TaxReasonProjection entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.taxation;

public interface TaxReasonProjection {
    Long getTaxReasonId();
    String getDisplaytext();
    String getDescription();
}