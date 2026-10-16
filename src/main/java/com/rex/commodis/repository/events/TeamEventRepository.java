/** Spring Data JPA repository for TeamEvent entities; extends JpaRepository and adds custom query methods. */
package com.rex.commodis.repository.events;
import com.rex.commodis.mapper.MySubmittedTeamEventGetListProjection;
import com.rex.commodis.model.enums.TeamEventStatusEnum;
import com.rex.commodis.repository.events.BookableEventProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rex.commodis.mapper.TeamEventGetListProjection;
import com.rex.commodis.model.events.TeamEvent;

import java.time.LocalDate;
import java.util.List;

public interface TeamEventRepository extends JpaRepository<TeamEvent, Long> {

    @Query("""
        SELECT
            te.id as teamEventId,
            FUNCTION('jsonb_extract_path_text', tes.displayText, :locale) as status,
            te.eventDate as eventDate,
            te.eventTitle as eventTitle,
            ru.fullName as submittedBy,
            pn.projectNumber as projectNumber,
            COALESCE(SUM(r.receiptAmount),0) as totalAmount
        
        FROM TeamEvent te
        
        JOIN TeamEventHistory teh
            ON teh.teamEvent = te
        
        JOIN teh.status tes
        
        LEFT JOIN te.rexUser ru
        LEFT JOIN te.projectNumber pn
        
        LEFT JOIN ReceiptToTeamEvent rtte
            ON rtte.teamEventId = te.id
        LEFT JOIN rtte.receipt r
        
        WHERE teh.active = true
        
        AND EXISTS (
            SELECT 1
            FROM TeamEventApprover tea
            WHERE tea.teamEvent = te
              AND tea.rexUser.id = :rexUserId
        )
        AND (
        :status IS NULL
        OR tes.statusCode = :status
        )
        
        AND (
         :eventTitle IS NULL
         OR LOWER(CAST(te.eventTitle as string))
            LIKE LOWER(CONCAT('%', CAST(:eventTitle as string), '%'))
        )
        AND (
         CAST
        (:submittedBy AS string) IS NULL
         OR LOWER(ru.fullName)
            LIKE LOWER(CONCAT('%', CAST(:submittedBy AS string), '%'))
        )
        AND (
        CAST(:projectNumber AS string) IS NULL
        OR LOWER(CAST(pn.projectNumber AS string))
           LIKE LOWER(CONCAT('%', CAST(:projectNumber AS string), '%'))
        )
        AND te.eventDate BETWEEN :fromDate AND :toDate
        
        GROUP BY
            te.id,
            tes.statusCode,
            tes.displayText,
            te.eventDate,
            te.eventTitle,
            ru.fullName,
            pn.projectNumber
        """)
    Page<TeamEventGetListProjection> getTeamEventsWithFiltersAndDates(
            @Param("rexUserId") Long rexUserId,
            @Param("status") TeamEventStatusEnum status,
            @Param("eventTitle") String eventTitle,
            @Param("submittedBy") String submittedBy,
            @Param("projectNumber") String projectNumber,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("locale") String locale,
            Pageable pageable
    );



    @Query("""
        SELECT
            te.id as teamEventId,
            FUNCTION('jsonb_extract_path_text', tes.displayText, :locale) as status,
            te.eventDate as eventDate,
            te.eventTitle as eventTitle,
            ru.fullName as submittedBy,
            pn.projectNumber as projectNumber,
            COALESCE(SUM(r.receiptAmount),0) as totalAmount
        
        FROM TeamEvent te
        
        JOIN TeamEventHistory teh
            ON teh.teamEvent = te
        
        JOIN teh.status tes
       
        LEFT JOIN te.rexUser ru
        LEFT JOIN te.projectNumber pn
        
        LEFT JOIN ReceiptToTeamEvent rtte
            ON rtte.teamEventId = te.id
        LEFT JOIN rtte.receipt r
        
        WHERE teh.active = true
        
        AND EXISTS (
            SELECT 1
            FROM TeamEventApprover tea
            WHERE tea.teamEvent = te
              AND tea.rexUser.id = :rexUserId
        )
        AND (
        :status IS NULL
        OR tes.statusCode = :status
        )
        
        AND (
         :eventTitle IS NULL
         OR LOWER(CAST(te.eventTitle as string))
        LIKE LOWER(CONCAT('%', CAST(:eventTitle as string), '%'))
        )
        AND (
         CAST
        (:submittedBy AS string) IS NULL
           OR LOWER(ru.fullName)
           LIKE LOWER(CONCAT('%', CAST(:submittedBy AS string), '%'))
        )
        AND (
        CAST(:projectNumber AS string) IS NULL
        OR LOWER(CAST(pn.projectNumber AS string))
           LIKE LOWER(CONCAT('%', CAST(:projectNumber AS string), '%'))
        )
    
        GROUP BY
            te.id,
            tes.statusCode,
            tes.displayText,
            te.eventDate,
            te.eventTitle,
            ru.fullName,
            pn.projectNumber
        """)
    Page<TeamEventGetListProjection> getTeamEventsWithFiltersNoDates(
            @Param("rexUserId") Long rexUserId,
            @Param("status") TeamEventStatusEnum status,
            @Param("eventTitle") String eventTitle,
            @Param("submittedBy") String submittedBy,
            @Param("projectNumber") String projectNumber,
            @Param("locale") String locale,
            Pageable pageable
    );

    @Query(value = """
        SELECT event_source as eventSource,
               event_id AS eventId,
               event_title::text AS eventTitle,
               inherit_participants AS inheritParticipants
        FROM events.bookable_events(:rexUserId)
        """, nativeQuery = true)
    List<BookableEventProjection> findBookableEvents(@Param("rexUserId") Long rexUserId);


    @Query(value = """
    SELECT DISTINCT

        te.id                                        AS teamEventId,
        
        te.teameventtype_id                          AS teameventtypeId,
            
        te.bookable_through                          AS bookableThrough,

        te.event_date                                AS eventDate,

        CAST(te.event_title AS text)                 AS eventTitle,

        CAST(te.internal_note AS text)               AS internalNote,

        CAST(ru.fullname AS text)                    AS submittedBy,
        
        tes.display_text ->> :locale as status,

        CAST(te.projectnumber_id AS text)               AS projectNumber,

        te.projectnumber_id                          AS projectNumberId,

        te.picklist_id                               AS picklistId,

        (
            SELECT COUNT(*) FROM rexbase.picklist_to_beneficiary ptb
            WHERE ptb.picklist_id = te.picklist_id
        ) + (
            SELECT COUNT(*) FROM rexbase.picklist_to_guest ptg
            WHERE ptg.picklist_id = te.picklist_id
        )                                            AS picklistMemberCount,

        r.id                                         AS receiptId,

        CAST(r.receipt_title AS text)                AS receiptTitle,

        r.receipt_date                               AS receiptDate,

        CAST(r.receiptnumber AS text)                AS receiptNumber,

        CAST(rr.fullname AS text)                    AS submittedFor,
        
        CAST(rs.display_text ->> :locale AS text) AS receiptStatus,

        r.receipt_amount                             AS receiptAmount,

        CAST(r.receipt_currency AS text)             AS receiptCurrency,

        r.gratuity                                   AS gratuity,

        r.amountnet_fullvat                          AS amountNetFullVat,

        r.vatrate_full                               AS vatRateFull,

        CAST(tc.display_text ->> :locale AS text)        AS expenseCategory,

        CAST(tr.display_text ->> :locale AS text)        AS expenseNature,

        CAST(ir.display_text ->> :locale AS text)                 AS issuanceRegion,

        r.deduct_team_balance                        AS deductTeamBalance,

        rf.receiptnumber_seq                         AS fileSequence,

        CASE
            WHEN pc.receipt_storage_link IS NOT NULL
            THEN CONCAT(
                pc.receipt_storage_link,
                '/',
                r.platform_id,
                '/',
                rf.receiptfileid
            )
            ELSE NULL
        END                                          AS fileUrl

    FROM events.team_event te

    JOIN rexbase.rexuser ru
        ON ru.id = te.rexuser_id

    LEFT JOIN rexbase.projectnumbers pn
        ON pn.id = te.projectnumber_id

    LEFT JOIN receipts.receipt_to_teamevent rtte
        ON rtte.teamevent_id = te.id

    LEFT JOIN receipts.receipts r
        ON r.id = rtte.receipt_id

    LEFT JOIN rexbase.rexuser rr
        ON rr.id = r.rexuser_id
                                                     
    LEFT JOIN receipts.receipt_history rh
            ON rh.receipt_id = r.id
            AND rh.active = true
    
    LEFT JOIN receipts.receipt_status rs
            ON rs.id = rh.receiptstatus_id

    LEFT JOIN taxation.taxreason_client tr
        ON tr.id = r.taxreason_client_id

    LEFT JOIN taxation.taxcategory_client tc
        ON tc.id = tr.taxcategory_client_id

    LEFT JOIN receipts.issuance_region ir
        ON ir.id = r.issuance_region_id

    LEFT JOIN receipts.receipt_files rf
        ON rf.receipt_id = r.id

    LEFT JOIN rexbase.platform_configuration pc
        ON pc.platform_id = te.platform_id
       AND CURRENT_DATE BETWEEN pc.activefrom AND pc.activethrough
                                                     
    LEFT JOIN events.team_event_history teh
                ON teh.teamevent_id = te.id
               AND teh.active = true
    
        LEFT JOIN events.team_event_status tes
                ON tes.id = teh.status_id

    WHERE te.id = :teamEventId

    AND EXISTS (
        SELECT 1
        FROM events.team_event_approver tea
        WHERE tea.teamevent_id = te.id
        AND tea.rexuser_id = :rexUserId
    )

    ORDER BY r.receipt_date DESC

""", nativeQuery = true)
    List<TeamEventDetailsProjection> getTeamEventDetails(
            @Param("teamEventId") Long teamEventId,
            @Param("rexUserId") Long rexUserId,
            @Param("locale") String locale
    );


	boolean existsByProjectNumberId(Long projectNumberId);

    @Query("""
    SELECT te FROM TeamEvent te
    WHERE te.bookableThrough < CURRENT_DATE
      AND te.processingReleasedAt IS NULL
    """)
    List<TeamEvent> findEventsToRelease();


    @Query("""
    SELECT
        te.id as teamEventId,
        FUNCTION('jsonb_extract_path_text', tes.displayText , :locale) as status,
        te.eventDate as eventDate,
        te.eventTitle as eventTitle,
        pn.projectNumber as projectNumber,
        COALESCE(SUM(r.receiptAmount),0) as totalAmount

    FROM TeamEvent te

    JOIN TeamEventHistory teh
        ON teh.teamEvent = te

    JOIN teh.status tes

    LEFT JOIN te.projectNumber pn

    LEFT JOIN ReceiptToTeamEvent rtte
        ON rtte.teamEventId = te.id

    LEFT JOIN rtte.receipt r

    WHERE teh.active = true

    AND te.rexuserId = :rexUserId

    AND (
        :status IS NULL
        OR tes.statusCode = :status
    )

    AND (
        :eventTitle IS NULL
        OR LOWER(CAST(te.eventTitle as string))
        LIKE LOWER(CONCAT('%',
             CAST(:eventTitle as string),
             '%'))
    )

    AND (
        CAST(:projectNumber AS string) IS NULL
        OR LOWER(CAST(pn.projectNumber as string))
        LIKE LOWER(CONCAT('%',
             CAST(:projectNumber as string),
             '%'))
    )

    AND te.eventDate
        BETWEEN :fromDate AND :toDate

    GROUP BY
        te.id,
        tes.statusCode,
        tes.displayText,
        te.eventDate,
        te.eventTitle,
        pn.projectNumber
""")
    Page<MySubmittedTeamEventGetListProjection>
    getMySubmittedTeamEventsWithFiltersAndDates(
            @Param("rexUserId") Long rexUserId,
            @Param("status") TeamEventStatusEnum status,
            @Param("eventTitle") String eventTitle,
            @Param("projectNumber") String projectNumber,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("locale") String locale,
            Pageable pageable
    );


    @Query("""
    SELECT
        te.id as teamEventId,
        FUNCTION('jsonb_extract_path_text', tes.displayText, :locale) as status,
        te.eventDate as eventDate,
        te.eventTitle as eventTitle,
        pn.projectNumber as projectNumber,
        COALESCE(SUM(r.receiptAmount),0) as totalAmount

    FROM TeamEvent te

    JOIN TeamEventHistory teh
        ON teh.teamEvent = te

    JOIN teh.status tes

    LEFT JOIN te.projectNumber pn

    LEFT JOIN ReceiptToTeamEvent rtte
        ON rtte.teamEventId = te.id

    LEFT JOIN rtte.receipt r

    WHERE teh.active = true

    AND te.rexuserId = :rexUserId

    AND (
        :status IS NULL
        OR tes.statusCode = :status
    )

    AND (
        :eventTitle IS NULL
        OR LOWER(CAST(te.eventTitle as string))
        LIKE LOWER(CONCAT('%',
             CAST(:eventTitle as string),
             '%'))
    )

    AND (
        CAST(:projectNumber AS string) IS NULL
        OR LOWER(CAST(pn.projectNumber as string))
        LIKE LOWER(CONCAT('%',
             CAST(:projectNumber as string),
             '%'))
    )

    GROUP BY
        te.id,
        tes.statusCode,
        tes.displayText,
        te.eventDate,
        te.eventTitle,
        pn.projectNumber
""")
    Page<MySubmittedTeamEventGetListProjection>
    getMySubmittedTeamEventsWithFiltersNoDates(
            @Param("rexUserId") Long rexUserId,
            @Param("status") TeamEventStatusEnum status,
            @Param("eventTitle") String eventTitle,
            @Param("projectNumber") String projectNumber,
            @Param("locale") String locale,
            Pageable pageable
    );

    @Query(value = """
    SELECT COUNT(*)
    FROM events.team_event
    WHERE picklist_id = :picklistId
      AND processing_id IS NOT NULL
""", nativeQuery = true)
    Integer countProcessedEvents(@Param("picklistId") Long picklistId);


    @Modifying
    @Query(value = """
    UPDATE events.team_event
    SET picklist_id = NULL,
        bookable_by = 'OWNER'
    WHERE picklist_id = :picklistId
      AND processing_id IS NULL
""", nativeQuery = true)
    void detachSafeTeamEvents(@Param("picklistId") Long picklistId);

}
