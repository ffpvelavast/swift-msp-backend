/** Spring Data JPA repository for ReceiptFile entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rex.commodis.model.receipts.ReceiptFile;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface ReceiptFileRepository extends JpaRepository<ReceiptFile, Long> {
    List<ReceiptFile> findByReceiptIdOrderByReceiptNumberSeq(Long receiptId);
}