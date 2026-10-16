/** Spring Data JPA repository for TaxCategory entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.taxation;

import com.rex.commodis.model.taxation.TaxCategoryClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxCategoryRepository extends JpaRepository<TaxCategoryClient, Long> {

    @Query(value = """
        SELECT COUNT(tcc.id) > 0
        FROM taxation.taxcategory_client tcc
        WHERE tcc.id = :taxCategoryId
        AND tcc.platform_id = :platformId
    """, nativeQuery = true)
    boolean existsCategoryForPlatform(
            @Param("taxCategoryId") Long taxCategoryId,
            @Param("platformId") Long platformId
    );

    @Query(value = """
        SELECT
            taxcategory_id  AS taxCategoryId,
            displaytext     AS displaytext,
            description     AS description
        FROM taxation.select_taxcategory(:platformId, :locale)
    """, nativeQuery = true)
    List<TaxCategoryProjection> findTaxCategories(
            @Param("platformId") Long platformId,
            @Param("locale") String locale
    );
}