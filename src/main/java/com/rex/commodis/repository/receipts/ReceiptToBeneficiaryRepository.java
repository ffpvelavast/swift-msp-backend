/** Spring Data JPA repository for ReceiptToBeneficiary entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.receipts.ReceiptHistory;
import com.rex.commodis.model.receipts.receiptToBeneficiary.ReceiptToBeneficiary;
import com.rex.commodis.model.receipts.receiptToBeneficiary.ReceiptToBeneficiaryId;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptToBeneficiaryRepository extends JpaRepository<ReceiptToBeneficiary, ReceiptToBeneficiaryId> {
    List<ReceiptToBeneficiary> findByReceiptId(Long receiptId);

    // Fetch internal beneficiary display names in one JOIN query
    @Query("""
            SELECT b.fullName
            FROM ReceiptToBeneficiary rtb
            JOIN rtb.beneficiary b
            WHERE rtb.receiptId = :receiptId
            """)
    List<String> findBeneficiaryNamesByReceiptId(@Param("receiptId") Long receiptId);

    }
