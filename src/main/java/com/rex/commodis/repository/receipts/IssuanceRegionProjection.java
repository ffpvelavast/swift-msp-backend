/** Spring Data JPA repository for IssuanceRegionProjection entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;

public interface IssuanceRegionProjection {
    Integer getIssuanceRegionId();
    String getRegionCode();
    String getDisplayText();
    String getDescription();
}
