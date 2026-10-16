/** Spring Data JPA repository for ReceiptStatus entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.enums.ReceiptStatusEnum;
import com.rex.commodis.model.receipts.ReceiptStatus;

@Repository
public interface ReceiptStatusRepository extends JpaRepository<ReceiptStatus, Long> {

    /**
     * Looks up a ReceiptStatus by matching the English value inside the JSONB
     * display_text column.
     *
     * Why native query: JPQL does not support PostgreSQL JSONB operators (->>'en').
     * No status_code column exists — this is the only correct lookup strategy
     * without modifying the schema.
     *
     * Example:
     *   input  → "Approved"
     *   SQL    → WHERE display_text->>'en' = 'Approved'
     *   result → ReceiptStatus{id=1, displayText={"en":"Approved","de":"Genehmigt"}}
     *
     * @param enDisplayText the English label stored inside the JSONB field
     */
    @Query(
            value = "SELECT * FROM receipts.receipt_status WHERE display_text->>'en' = :enDisplayText",
            nativeQuery = true
    )
    Optional<ReceiptStatus> findByDisplayTextEnValue(@Param("enDisplayText") String enDisplayText);
    
    Optional<ReceiptStatus> findByStatus(ReceiptStatusEnum status);

}