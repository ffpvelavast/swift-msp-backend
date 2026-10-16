/** Spring Data JPA repository for Receipt entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.receipts;

import com.rex.commodis.dto.ReceiptGetListApproverProjectionDto;
import com.rex.commodis.dto.ReceiptGetListSubmitterProjectionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.rex.commodis.dto.ReceiptStatusCountProjection;
import com.rex.commodis.model.receipts.Receipt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query(value = """
        SELECT
            r.id                      AS id,
            r.receipt_title::text     AS receiptTitle,
            u.fullname::text          AS submittedBy,
            r.receipt_date            AS receiptDate,
            r.receiptnumber           AS receiptNumber,
            r.receipt_amount          AS receiptAmount,
            r.receipt_currency         AS currency,
            rs.display_text ->> CAST(:locale AS text) AS status,
            tc.display_text ->> CAST(:locale AS text) AS category
        FROM receipts.receipts r
        JOIN rexbase.rexuser u
            ON u.id = r.rexuser_id
        JOIN receipts.receipt_history rh
            ON rh.receipt_id = r.id AND rh.active = true
        JOIN receipts.receipt_status rs
            ON rs.id = rh.receiptstatus_id
        LEFT JOIN taxation.taxreason_client tr
            ON tr.id = r.taxreason_client_id
        
        LEFT JOIN taxation.taxcategory_client tc
            ON tc.id = tr.taxcategory_client_id
   
        WHERE (
            r.rexuser_id = :rexUserId
        )
    
        AND (CAST(:status AS text) IS NULL 
             OR rs.status_code = CAST(:status AS text))
    
        AND (CAST(:receiptTitle AS text) IS NULL
             OR r.receipt_title ILIKE '%' || CAST(:receiptTitle AS text) || '%')
    
        AND (CAST(:submittedBy AS text) IS NULL
             OR u.fullname ILIKE '%' || CAST(:submittedBy AS text) || '%')
    
        AND (CAST(:receiptAmount AS numeric) IS NULL 
             OR r.receipt_amount = :receiptAmount)
    
        AND (
            CAST(:fromDate AS date) IS NULL
            OR CAST(:toDate AS date) IS NULL
            OR r.receipt_date BETWEEN :fromDate AND :toDate
        )
        
        AND (
            CAST(:category AS text) IS NULL
            OR (tc.display_text ->>  CAST(:locale AS text) ) ILIKE '%' || CAST(:category AS text) || '%'
        )
            
        ORDER BY
        
        -- RECEIPT TITLE
        CASE
            WHEN :sortBy = 'receipt_title' AND :direction = 'ASC'
            THEN r.receipt_title
        END ASC,
        CASE
            WHEN :sortBy = 'receipt_title' AND :direction = 'DESC'
            THEN r.receipt_title
        END DESC,
        
        -- SUBMITTED FOR
        CASE
            WHEN :sortBy = 'submitted_by' AND :direction = 'ASC'
            THEN u.fullname
        END ASC,
        CASE
            WHEN :sortBy = 'submitted_by' AND :direction = 'DESC'
            THEN u.fullname
        END DESC,
        
        -- RECEIPT DATE
        CASE
            WHEN :sortBy = 'receipt_date' AND :direction = 'ASC'
            THEN r.receipt_date
        END ASC,
        CASE
            WHEN :sortBy = 'receipt_date' AND :direction = 'DESC'
            THEN r.receipt_date
        END DESC,
        
        -- CATEGORY
        CASE
            WHEN :sortBy = 'category' AND :direction = 'ASC'
            THEN (tc.display_text ->>  CAST(:locale AS text) )
        END ASC,
        CASE
            WHEN :sortBy = 'category' AND :direction = 'DESC'
            THEN (tc.display_text ->>  CAST(:locale AS text) )
        END DESC,
        
        -- AMOUNT
        CASE
            WHEN :sortBy = 'receipt_amount' AND :direction = 'ASC'
            THEN r.receipt_amount
        END ASC,
        CASE
            WHEN :sortBy = 'receipt_amount' AND :direction = 'DESC'
            THEN r.receipt_amount
        END DESC,
        
        -- STATUS
        CASE
            WHEN :sortBy = 'status' AND :direction = 'ASC'
            THEN rs.status_code
        END ASC,
        CASE
            WHEN :sortBy = 'status' AND :direction = 'DESC'
            THEN rs.status_code
        END DESC,
        
        -- DEFAULT FALLBACK
        r.receipt_date DESC
  
            
    """, nativeQuery = true)
    Page<ReceiptGetListSubmitterProjectionDto> findSubmitterReceiptsWithFilters(
            @Param("rexUserId")     Long rexUserId,
            @Param("status")        String status,
            @Param("receiptTitle")  String receiptTitle,
            @Param("submittedBy")   String submittedBy,
            @Param("category")      String category,
            @Param("locale")        String locale,
            @Param("receiptAmount") BigDecimal receiptAmount,
            @Param("fromDate")      LocalDate fromDate,
            @Param("toDate")        LocalDate toDate,
            @Param("sortBy")        String sortBy,
            @Param("direction")     String direction,
            Pageable pageable
    );

    @Query(value = """
        SELECT
            r.id                      AS id,
            r.receipt_title::text     AS receiptTitle,
            u.fullname::text          AS submittedFor,
            r.receipt_date            AS receiptDate,
            r.receiptnumber           AS receiptNumber,
            r.receipt_amount          AS receiptAmount,
            r.receipt_currency        AS currency,
            rs.display_text ->> CAST(:locale AS text) AS status,
            tc.display_text ->> CAST(:locale AS text) AS category
        FROM receipts.receipts r
        JOIN rexbase.rexuser u
            ON u.id = r.rexuser_id
        JOIN receipts.receipt_history rh
            ON rh.receipt_id = r.id AND rh.active = true
        JOIN receipts.receipt_status rs
            ON rs.id = rh.receiptstatus_id
        LEFT JOIN taxation.taxreason_client tr
            ON tr.id = r.taxreason_client_id
        
        LEFT JOIN taxation.taxcategory_client tc
            ON tc.id = tr.taxcategory_client_id
   
        WHERE EXISTS (
            SELECT 1
            FROM receipts.receipt_approver ra
            WHERE ra.receipt_id = r.id
              AND ra.rexuser_id = :rexUserId
        )
    
        AND NOT EXISTS (
            SELECT 1
            FROM receipts.receipt_to_teamevent rte
            WHERE rte.receipt_id = r.id
        )
    
        AND (CAST(:status AS text) IS NULL 
             OR rs.status_code = CAST(:status AS text))
    
        AND (CAST(:receiptTitle AS text) IS NULL
             OR r.receipt_title ILIKE '%' || CAST(:receiptTitle AS text) || '%')
    
        AND (CAST(:submittedFor AS text) IS NULL
             OR u.fullname ILIKE '%' || CAST(:submittedFor AS text) || '%')
    
        AND (CAST(:receiptAmount AS numeric) IS NULL 
             OR r.receipt_amount = :receiptAmount)
    
        AND (
            CAST(:fromDate AS date) IS NULL
            OR CAST(:toDate AS date) IS NULL
            OR r.receipt_date BETWEEN :fromDate AND :toDate
        )
        
       AND (
           CAST(:category AS text) IS NULL
           OR (tc.display_text ->>  CAST(:locale AS text) ) ILIKE '%' || CAST(:category AS text) || '%'
       ) 
    
       ORDER BY
    
       -- RECEIPT TITLE
       CASE
           WHEN :sortBy = 'receipt_title' AND :direction = 'ASC'
           THEN r.receipt_title
       END ASC,
       CASE
           WHEN :sortBy = 'receipt_title' AND :direction = 'DESC'
           THEN r.receipt_title
       END DESC,
       
       -- SUBMITTED FOR
       CASE
           WHEN :sortBy = 'submitted_for' AND :direction = 'ASC'
           THEN u.fullname
       END ASC,
       CASE
           WHEN :sortBy = 'submitted_for' AND :direction = 'DESC'
           THEN u.fullname
       END DESC,
       
       -- RECEIPT DATE
       CASE
           WHEN :sortBy = 'receipt_date' AND :direction = 'ASC'
           THEN r.receipt_date
       END ASC,
       CASE
           WHEN :sortBy = 'receipt_date' AND :direction = 'DESC'
           THEN r.receipt_date
       END DESC,
       
       -- CATEGORY
       CASE
           WHEN :sortBy = 'category' AND :direction = 'ASC'
           THEN (tc.display_text ->>  CAST(:locale AS text) )
       END ASC,
       CASE
           WHEN :sortBy = 'category' AND :direction = 'DESC'
           THEN (tc.display_text ->>  CAST(:locale AS text) )
       END DESC,
       
       -- AMOUNT
       CASE
           WHEN :sortBy = 'receipt_amount' AND :direction = 'ASC'
           THEN r.receipt_amount
       END ASC,
       CASE
           WHEN :sortBy = 'receipt_amount' AND :direction = 'DESC'
           THEN r.receipt_amount
       END DESC,
       
       -- STATUS
       CASE
           WHEN :sortBy = 'status' AND :direction = 'ASC'
           THEN rs.status_code
       END ASC,
       CASE
           WHEN :sortBy = 'status' AND :direction = 'DESC'
           THEN rs.status_code
       END DESC,
       
       -- DEFAULT FALLBACK
       r.receipt_date DESC
            
    """, nativeQuery = true)
    Page<ReceiptGetListApproverProjectionDto> findApproverReceiptsWithFilters(
            @Param("rexUserId")     Long rexUserId,
            @Param("status")        String status,
            @Param("receiptTitle")  String receiptTitle,
            @Param("submittedFor")   String submittedFor,
            @Param("category")      String category,
            @Param("locale")        String locale,
            @Param("receiptAmount") BigDecimal receiptAmount,
            @Param("fromDate")      LocalDate fromDate,
            @Param("toDate")        LocalDate toDate,
            @Param("sortBy")        String sortBy,
            @Param("direction")     String direction,
            Pageable pageable
    );


    @Query(value = """
        SELECT
            rs.status_code::text AS status,
            COUNT(*)             AS count
        FROM receipts.receipts r
        JOIN receipts.receipt_history rh
            ON rh.receipt_id = r.id
           AND rh.active = true
        JOIN receipts.receipt_status rs
            ON rs.id = rh.receiptstatus_id
        WHERE r.rexuser_id = :rexUserId
        GROUP BY rs.status_code
    """, nativeQuery = true)
    List<ReceiptStatusCountProjection> getSubmitterStatusCounts(
            @Param("rexUserId") Long rexUserId
    );

    @Query(value = """
        SELECT
            rs.status_code::text AS status,
            COUNT(*)             AS count
        FROM receipts.receipts r
        JOIN receipts.receipt_history rh
            ON rh.receipt_id = r.id
           AND rh.active = true
        JOIN receipts.receipt_status rs
            ON rs.id = rh.receiptstatus_id
        WHERE EXISTS (
            SELECT 1
            FROM receipts.receipt_approver ra
            WHERE ra.receipt_id = r.id
              AND ra.rexuser_id = :rexUserId
        )
        AND NOT EXISTS (
            SELECT 1
            FROM receipts.receipt_to_teamevent rte
            WHERE rte.receipt_id = r.id
        )
        GROUP BY rs.status_code
    """, nativeQuery = true)
    List<ReceiptStatusCountProjection> getApproverStatusCounts(
            @Param("rexUserId") Long rexUserId
    );

    Boolean existsByReceiptNumber(String receiptNumber);
    Boolean existsByProjectNumberId(Long projectNumberId);

    @Query(value = """
        SELECT COUNT(*)
        FROM receipts.receipts r
        JOIN receipts.receipt_history rh
            ON rh.receipt_id = r.id
        AND rh.active = true
        JOIN receipts.receipt_status rs
            ON rs.id = rh.receiptstatus_id
        WHERE EXISTS (
            SELECT 1
            FROM receipts.receipt_approver ra
        WHERE ra.receipt_id = r.id
             AND ra.rexuser_id = :rexUserId
        )
        AND NOT EXISTS (
            SELECT 1
            FROM receipts.receipt_to_teamevent rte
            WHERE rte.receipt_id = r.id
        )
        AND rs.status_code IN (
            'SUBMITTED',
            'RESUBMITTED',
            'CORRECTION_REQUESTED'
        )
    """, nativeQuery = true)
    Long getApproverReceiptCount(
            @Param("rexUserId") Long rexUserId
    );
}