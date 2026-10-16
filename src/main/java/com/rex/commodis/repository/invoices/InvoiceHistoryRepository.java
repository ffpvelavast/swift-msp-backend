/** Spring Data JPA repository for InvoiceHistory entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.invoices;

import com.rex.commodis.dto.invoiceDtos.InvoiceDashboardListProjection;
import com.rex.commodis.model.enums.InvoiceStatusEnum;
import com.rex.commodis.model.invoices.InvoiceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceHistoryRepository extends JpaRepository<InvoiceHistory, Long> {

    Optional<InvoiceHistory> findTopByInvoiceIdOrderByCreatedAtDesc(Long invoiceId);

    @Query(value = """
        SELECT COUNT(*) AS invoices_count
        FROM invoices.invoice_history ih
        JOIN invoices.invoice_status ist
            ON ist.id = ih.status_id
        JOIN invoices.invoice_responsibility ir
            ON ih.invoice_id = ir.invoice_id
        WHERE ist.invoice_status_enum IN ('PENDING', 'DRAFT')
          AND ir.delegate_id = :rexUserId
          AND ir.is_active = TRUE
          AND ih.active = TRUE
    """, nativeQuery = true)
    Long getDashboardInvoicesCount(@Param("rexUserId") Long rexUserId);

    @Query(value = """
        SELECT
            i.id                          AS invoiceId,
            i.vendor::text                AS vendorName,
            i.invoicetotal_gross          AS invoiceTotal,
            i.currency::text              AS currency,
            COALESCE(
                ist.display_text ->> lower(:locale),
                ist.display_text ->> 'en'
            ) AS status
        FROM invoices.invoice_history ih
    
        JOIN invoices.invoice_status ist
            ON ih.status_id = ist.id
    
        JOIN invoices.invoices i
            ON ih.invoice_id = i.id
    
        JOIN invoices.invoice_responsibility ir
            ON ih.invoice_id = ir.invoice_id
    
        WHERE ist.invoice_status_enum IN ('PENDING', 'DRAFT')
          AND ir.delegate_id = :rexUserId
          AND ir.is_active = TRUE
          AND ih.active = TRUE
    
        ORDER BY i.created_at DESC
        LIMIT 5
    """, nativeQuery = true)
    List<InvoiceDashboardListProjection> getDashboardInvoicesList(
            @Param("rexUserId") Long rexUserId,
            @Param("locale") String locale
    );

    @Query("""
            SELECT h FROM InvoiceHistory h
            JOIN h.status s
            WHERE h.invoiceId = :invoiceId
              AND s.status   = :status
              AND h.active   = true
            """)
    Optional<InvoiceHistory> findActiveByInvoiceIdAndStatus(
            @Param("invoiceId") Long invoiceId,
            @Param("status") InvoiceStatusEnum status);


    @Modifying
    @Query("""
        UPDATE InvoiceHistory ih
        SET ih.active = false
        WHERE ih.invoiceId = :invoiceId
        AND ih.active = true
""")
    int deactivateCurrentActiveHistory(@Param("invoiceId") Long invoiceId);
}
