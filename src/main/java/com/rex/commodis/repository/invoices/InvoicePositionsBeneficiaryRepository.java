package com.rex.commodis.repository.invoices;

import com.rex.commodis.model.invoices.InvoicePositionsBeneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoicePositionsBeneficiaryRepository extends JpaRepository<InvoicePositionsBeneficiary, Long> {

    @Query("SELECT b.beneficiaryId FROM InvoicePositionsBeneficiary b WHERE b.invoicePositionId = :positionId AND b.isDeleted = false")
    List<Long> findActiveIdsByInvoicePositionId(@Param("positionId") Long positionId);

    @Modifying
    @Query("""
        UPDATE InvoicePositionsBeneficiary b
        SET b.isDeleted = true
        WHERE b.invoicePositionId = :positionId
        AND b.beneficiaryId IN :beneficiaryIds
        AND b.isDeleted = false
    """)
    void softDeleteByPositionIdAndBeneficiaryIds(@Param("positionId") Long positionId, @Param("beneficiaryIds") List<Long> beneficiaryIds);
}
