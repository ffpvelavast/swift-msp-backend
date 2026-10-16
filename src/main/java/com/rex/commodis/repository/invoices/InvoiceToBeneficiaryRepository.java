package com.rex.commodis.repository.invoices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.invoices.InvoiceToBeneficiary;

import java.util.List;

@Repository
public interface InvoiceToBeneficiaryRepository extends JpaRepository<InvoiceToBeneficiary, Long> {

    // Fetch all records (active + soft-deleted) for sync logic
    List<InvoiceToBeneficiary> findByInvoiceId(Long invoiceId);

    @Modifying
    @Query("""
            DELETE FROM InvoiceToBeneficiary b
            WHERE b.invoiceId = :invoiceId
              AND b.beneficiaryId NOT IN :beneficiaryIds
            """)
    void deleteByInvoiceIdAndBeneficiaryIdNotIn(
            @Param("invoiceId") Long invoiceId,
            @Param("beneficiaryIds") List<Long> beneficiaryIds);

    @Modifying
    @Query("""
            DELETE FROM InvoiceToBeneficiary b
            WHERE b.invoiceId = :invoiceId
            """)
    void deleteAllByInvoiceId(@Param("invoiceId") Long invoiceId);

    @Query("""
            SELECT b.beneficiaryId
            FROM InvoiceToBeneficiary b
            WHERE b.invoiceId = :invoiceId
            """)
    List<Long> findBeneficiaryIdsByInvoiceId(@Param("invoiceId") Long invoiceId);
}
