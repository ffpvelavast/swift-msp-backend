package com.rex.commodis.repository.invoices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.invoices.InvoiceToGuest;

import java.util.List;

@Repository
public interface InvoiceToGuestRepository extends JpaRepository<InvoiceToGuest, Long> {

    List<InvoiceToGuest> findByInvoiceId(Long invoiceId);

    @Modifying
    @Query("""
            DELETE FROM InvoiceToGuest g
            WHERE g.invoiceId = :invoiceId
              AND g.guestId NOT IN :guestIds
            """)
    void deleteByInvoiceIdAndGuestIdNotIn(
            @Param("invoiceId") Long invoiceId,
            @Param("guestIds") List<Long> guestIds);

    @Modifying
    @Query("""
            DELETE FROM InvoiceToGuest g
            WHERE g.invoiceId = :invoiceId
            """)
    void deleteAllByInvoiceId(@Param("invoiceId") Long invoiceId);

    @Query("""
            SELECT g.guestId
            FROM InvoiceToGuest g
            WHERE g.invoiceId = :invoiceId
            """)
    List<Long> findGuestIdsByInvoiceId(@Param("invoiceId") Long invoiceId);
}
