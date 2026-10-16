/** Spring Data JPA repository for IssuanceRegion entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.receipts.IssuanceRegion;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssuanceRegionRepository extends JpaRepository<IssuanceRegion, Short> {

    @Query(value = """
        SELECT 
            issuance_region_id AS issuanceRegionId, 
            region_code AS regionCode,
            displaytext AS displayText,
            description AS description
        FROM receipts.select_receipt_issuanceregion(:platformId, :locale)
    """, nativeQuery = true)
    List<IssuanceRegionProjection> findIssuanceRegions(
            @Param("platformId") Long platformId,
            @Param("locale") String locale
    );
}