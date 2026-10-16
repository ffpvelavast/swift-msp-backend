package com.rex.commodis.repository.invoices;

import com.rex.commodis.model.invoices.InvoicePositionsGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoicePositionsGuestRepository extends JpaRepository<InvoicePositionsGuest, Long> {

    @Query("SELECT g.guestId FROM InvoicePositionsGuest g WHERE g.invoicePositionId = :positionId AND g.isDeleted = false")
    List<Long> findActiveIdsByInvoicePositionId(@Param("positionId") Long positionId);

    @Modifying
    @Query("""
        UPDATE InvoicePositionsGuest g
        SET g.isDeleted = true
        WHERE g.invoicePositionId = :positionId
        AND g.guestId IN :guestIds
        AND g.isDeleted = false
    """)
    void softDeleteByPositionIdAndGuestIds(@Param("positionId") Long positionId, @Param("guestIds") List<Long> guestIds);
}
