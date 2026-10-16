/** Spring Data JPA repository for Invoice entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.invoices;

import com.rex.commodis.dto.invoiceDtos.InvoiceDelegateDetailsProjection;
import com.rex.commodis.dto.invoiceDtos.InvoiceDetailsResponseDto;
import com.rex.commodis.dto.invoiceDtos.InvoiceGetListProjectionDto;
import com.rex.commodis.dto.invoiceDtos.InvoiceStatusCountProjection;
import com.rex.commodis.model.enums.InvoiceStatusEnum;
import com.rex.commodis.model.invoices.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

//    boolean existsByProjectNumberId(Long projectNumberId);
//
//    // Query WITH date filter
//    @Query("""
//        SELECT
//            i.id                    AS id,
//            i.vendor                AS vendor,
//            i.documentName          AS documentName,
//            i.documentDate          AS documentDate,
//            i.vendorInvoiceNumber   AS vendorInvoiceNumber,
//            i.invoiceTotalGross     AS invoiceTotalGross,
//            i.currency              AS currency,
//            s.status                AS status
//        FROM Invoice i
//        JOIN InvoiceResponsibility r ON r.invoiceId = i.id
//        JOIN InvoiceStatus s ON s.invoiceId = i.id
//        WHERE r.delegateId = :rexUserId
//        AND r.isActive = true
//        AND (:status IS NULL OR LOWER(s.status) = LOWER(CAST(:status AS string)))
//        AND (:vendor IS NULL OR LOWER(i.vendor) LIKE LOWER(CONCAT('%', CAST(:vendor AS string), '%')))
//        AND i.documentDate BETWEEN :fromDate AND :toDate
//        AND s.id = (SELECT MAX(s2.id) FROM InvoiceStatus s2 WHERE s2.invoiceId = i.id)
//    """)
//    Page<InvoiceGetListProjectionDto> findInvoicesForUserWithFiltersAndDates(
//            @Param("rexUserId") Long rexUserId,
//            @Param("status") String status,
//            @Param("vendor") String vendor,
//            @Param("fromDate") LocalDate fromDate,
//            @Param("toDate") LocalDate toDate,
//            Pageable pageable
//    );
//
//    // Query WITHOUT date filter
//    @Query("""
//        SELECT
//            i.id                    AS id,
//            i.vendor                AS vendor,
//            i.documentName          AS documentName,
//            i.documentDate          AS documentDate,
//            i.vendorInvoiceNumber   AS vendorInvoiceNumber,
//            i.invoiceTotalGross     AS invoiceTotalGross,
//            i.currency              AS currency,
//            s.status                AS status
//        FROM Invoice i
//        JOIN InvoiceResponsibility r ON r.invoiceId = i.id
//        JOIN InvoiceStatus s ON s.invoiceId = i.id
//        WHERE r.delegateId = :rexUserId
//        AND r.isActive = true
//        AND (:status IS NULL OR LOWER(s.status) = LOWER(CAST(:status AS string)))
//        AND (:vendor IS NULL OR LOWER(i.vendor) LIKE LOWER(CONCAT('%', CAST(:vendor AS string), '%')))
//        AND s.id = (SELECT MAX(s2.id) FROM InvoiceStatus s2 WHERE s2.invoiceId = i.id)
//    """)
//    Page<InvoiceGetListProjectionDto> findInvoicesForUserWithFiltersNoDates(
//            @Param("rexUserId") Long rexUserId,
//            @Param("status") String status,
//            @Param("vendor") String vendor,
//            Pageable pageable
//    );
//
//    @Query(value = """
//        WITH latest_status AS (
//            SELECT DISTINCT ON (invoice_id)
//                invoice_id,
//                status
//            FROM invoices.invoice_status
//            ORDER BY invoice_id, created_at DESC
//        )
//        SELECT ls.status AS status, COUNT(*) AS count
//        FROM latest_status ls
//        JOIN invoices.invoice_responsibility r
//            ON r.invoice_id = ls.invoice_id
//        WHERE r.delegate_id = :rexUserId
//          AND r.is_active = true
//        GROUP BY ls.status
//    """, nativeQuery = true) //Native PostgreSQL-query not JPQL
//    List<InvoiceStatusCountProjection> getStatusCounts(
//            @Param("rexUserId") Long rexUserId
//    );
//
//
//    // Delegated invoices (with date)
//    @Query("""
//        SELECT
//            i.id                    AS id,
//            i.vendor                AS vendor,
//            i.documentName          AS documentName,
//            i.documentDate          AS documentDate,
//            i.documentNumber        AS documentNumber,
//            i.invoiceTotalGross     AS invoiceTotalGross,
//            i.currency              AS currency,
//            s.status                AS status,
//            irActive.delegateId     AS delegateId
//        FROM Invoice i
//        JOIN InvoiceResponsibility irOld
//             ON irOld.invoiceId = i.id
//        JOIN InvoiceResponsibility irActive
//             ON irOld.invoiceId = irActive.invoiceId
//        JOIN InvoiceStatus s
//             ON s.invoiceId = i.id
//        WHERE irOld.rexUserId = :rexUserId
//          AND irActive.isActive = true
//          AND irActive.delegateId != :rexUserId
//          AND i.documentDate BETWEEN :from AND :to
//          AND s.createdAt = (
//                  SELECT MAX(s2.createdAt)
//                  FROM InvoiceStatus s2
//                  WHERE s2.invoiceId = i.id
//            )
//          AND s.status IN :statuses
//    """)
//    Page<InvoiceDelegateDetailsProjection> findDelegatedInvoicesByDateRange(
//            @Param("rexUserId") Long rexUserId,
//            @Param("from") LocalDate from,
//            @Param("to") LocalDate to,
//            @Param("statuses") List<InvoiceStatusEnum> statuses,
//            Pageable pageable
//    );
//
//    // Delegated invoices (with date)
//    @Query("""
//        SELECT
//            i.id                    AS id,
//            i.vendor                AS vendor,
//            i.documentName          AS documentName,
//            i.documentDate          AS documentDate,
//            i.documentNumber        AS documentNumber,
//            i.invoiceTotalGross     AS invoiceTotalGross,
//            i.currency              AS currency,
//            s.status                AS status,
//            irActive.delegateId     AS delegateId
//        FROM Invoice i
//        JOIN InvoiceResponsibility irOld
//             ON irOld.invoiceId = i.id
//        JOIN InvoiceResponsibility irActive
//             ON irOld.invoiceId = irActive.invoiceId
//        JOIN InvoiceStatus s
//             ON s.invoiceId = i.id
//        WHERE irOld.rexUserId = :rexUserId
//          AND irActive.isActive = true
//          AND irActive.delegateId != :rexUserId
//          AND s.createdAt = (
//                  SELECT MAX(s2.createdAt)
//                  FROM InvoiceStatus s2
//                  WHERE s2.invoiceId = i.id
//            )
//          AND s.status IN :statuses
//    """)
//    Page<InvoiceDelegateDetailsProjection> findDelegatedInvoices(
//            @Param("rexUserId") Long rexUserId,
//            @Param("statuses") List<InvoiceStatusEnum> statuses,
//            Pageable pageable
//    );
//
//
//
//    // Count delegated invoices
//    @Query("""
//        SELECT COUNT(DISTINCT i.id)
//        FROM Invoice i
//        JOIN InvoiceResponsibility irOld
//             ON irOld.invoiceId = i.id
//        JOIN InvoiceResponsibility irActive
//             ON irOld.invoiceId = irActive.invoiceId
//        JOIN InvoiceStatus s
//             ON s.invoiceId = i.id
//        WHERE irOld.rexUserId = :rexUserId
//          AND irActive.isActive = true
//          AND irActive.delegateId != :rexUserId
//          AND s.createdAt = (
//                  SELECT MAX(s2.createdAt)
//                  FROM InvoiceStatus s2
//                  WHERE s2.invoiceId = i.id
//            )
//          AND s.status IN :statuses
//    """)
//    long countDelegatedInvoices(
//            @Param("rexUserId") Long rexUserId,
//            @Param("statuses") List<InvoiceStatusEnum> statuses
//    );
//
//    @Query("""
//        SELECT new com.rex.commodis.dto.InvoiceDetailsResponseDto(
//            i.id,
//            i.documentName,
//            i.documentNumber,
//            i.vendor,
//            i.currency,
//            i.invoiceTotalNet,
//            i.invoiceTotalVat,
//            i.invoiceTotalGross,
//            CAST(s.status AS string),
//            u.email,
//            s.createdAt,
//            i.note
//        )
//        FROM Invoice i
//        LEFT JOIN InvoiceStatus s ON s.invoiceId = i.id
//        LEFT JOIN s.statusCreator u
//        WHERE i.id = :invoiceId
//        AND s.id = (
//            SELECT MAX(s2.id) FROM InvoiceStatus s2 WHERE s2.invoiceId = i.id
//        )
//    """)
//    Optional<InvoiceDetailsResponseDto> getInvoiceDetails(Long invoiceId);
//
//    @Modifying(flushAutomatically = true)
//    @Query("UPDATE Invoice i SET i.projectNumberId = :projectNumberId WHERE i.id = :invoiceId")
//    void updateProjectNumberId(Long invoiceId, Long projectNumberId);

    boolean existsByProjectNumberId(Long projectNumberId);

    @Modifying
    @Query("UPDATE Invoice i SET i.note = :note WHERE i.id = :invoiceId")
    void updateNote(@Param("invoiceId") Long invoiceId, @Param("note") String note);
}


