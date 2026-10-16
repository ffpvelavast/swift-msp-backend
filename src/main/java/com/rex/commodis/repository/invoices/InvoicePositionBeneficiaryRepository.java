/** Spring Data JPA repository for InvoicePositionBeneficiary entities; extends JpaRepository and adds custom query methods. */
//
//
//package com.rex.commodis.repository;
//
//import com.rex.boilerplate.springboot.model.InvoicePositionBeneficiary;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface InvoicePositionBeneficiaryRepository
//        extends JpaRepository<InvoicePositionBeneficiary, Long> {
//
//
//    List<InvoicePositionBeneficiary> findByInvoicePositionIdAndIsDeletedFalse(Long invoicePositionId);
//
//    // Moved from InvoicePositionRepository - belongs here
//    Optional<InvoicePositionBeneficiary> findByInvoicePositionIdAndBeneficiaryId(
//            Long invoicePositionId, Long beneficiaryId);
//
//    // Moved from InvoicePositionRepository - belongs here
//
//    @Modifying
//    @Query("""
//        UPDATE InvoicePositionBeneficiary b
//        SET b.isDeleted = true
//        WHERE b.invoicePositionId = :positionId
//        AND b.beneficiaryId = :beneficiaryId
//    """)
//    void softDeleteByPositionAndBeneficiary(
//            @Param("positionId") Long positionId,
//            @Param("beneficiaryId") Long beneficiaryId
//    );
//
//    @Query("""
//            SELECT DISTINCT ipb.beneficiaryId
//            FROM InvoicePositionBeneficiary ipb
//            JOIN InvoicePosition ip
//                ON ip.id = ipb.invoicePositionId
//            WHERE ip.invoiceId = :invoiceId
//            AND ipb.isDeleted = false
//            """)
//    List<Long> findActiveBeneficiaryIdsByInvoiceId(
//            @Param("invoiceId") Long invoiceId
//    );
//}


