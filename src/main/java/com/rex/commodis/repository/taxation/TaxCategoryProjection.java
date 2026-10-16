/** Spring Data JPA repository for TaxCategoryProjection entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.taxation;

public interface TaxCategoryProjection {
    Long getTaxCategoryId();
    String getDisplaytext();
    String getDescription();
}