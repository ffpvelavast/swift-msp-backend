/** Spring Data JPA repository for TaxReason entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.taxation;

import com.rex.commodis.model.taxation.TaxReasonClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxReasonRepository extends JpaRepository<TaxReasonClient, Long> {

    @Query(value = """
	SELECT
	        tr.taxreason_id AS taxReasonId,
	        tr.displaytext  AS displaytext,
	        tr.description  AS description
	    FROM taxation.select_taxreason(:taxCategoryId, :locale) tr
	    JOIN taxation.taxreason_client trc
	        ON trc.id = tr.taxreason_id
	    WHERE trc.eligible_receipt = true
    """, nativeQuery = true)
    List<TaxReasonProjection> findTaxReasons(
            @Param("taxCategoryId") Long taxCategoryId,
            @Param("locale") String locale
    );
}