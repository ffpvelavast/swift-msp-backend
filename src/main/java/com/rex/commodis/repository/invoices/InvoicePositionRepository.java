/** Spring Data JPA repository for InvoicePosition entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.invoices;

import com.rex.commodis.model.*;
import com.rex.commodis.model.invoices.InvoicePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface InvoicePositionRepository extends JpaRepository<InvoicePosition, Long> {

    boolean existsByIdAndInvoiceId(Long id, Long invoiceId);

    @Modifying
    @Query("UPDATE InvoicePosition p SET p.taxReasonClientId = :taxReasonId WHERE p.id = :positionId")
    void updateTaxReasonClientId(@Param("positionId") Long positionId, @Param("taxReasonId") Long taxReasonId);

//    List<InvoicePosition> findByInvoiceId(Long invoiceId);
//
//    @Query("""
//        SELECT DISTINCT ipb.beneficiary
//        FROM InvoicePosition ip
//        JOIN ip.beneficiaries ipb
//        WHERE ip.invoiceId = :invoiceId
//        AND ipb.isDeleted = false
//    """)
//    List<Beneficiary> findBeneficiariesByInvoiceId(Long invoiceId);

    @Query("""
        SELECT DISTINCT ip.projectNumber
        FROM InvoicePosition ip
        WHERE ip.invoiceId = :invoiceId
          AND ip.projectNumber IS NOT NULL
    """)
    Set<String> findDistinctProjectNumbers(Long invoiceId);

    boolean existsByProjectNumberId(Long projectNumberId);

}
