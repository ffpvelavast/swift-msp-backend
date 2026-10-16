----------------------------------------------------------------------------
------------------------ Views for taxaxtion  ------------------------------
----------------------------------------------------------------------------

DROP VIEW IF EXISTS taxation.teamevents_unprocessed;
DROP VIEW IF EXISTS taxation.receipts_unprocessed;

----------------------------------------------------------------------------

CREATE OR REPLACE VIEW taxation.receipts_unprocessed
 AS
 WITH count_beneficiary AS (
         SELECT receipt_to_beneficiary.receipt_id,
            count(*) AS rec_count
           FROM receipts.receipt_to_beneficiary
          GROUP BY receipt_to_beneficiary.receipt_id
        ), count_guest AS (
         SELECT receipt_to_guest.receipt_id,
            count(*) AS rec_count
           FROM receipts.receipt_to_guest
          GROUP BY receipt_to_guest.receipt_id
        )
 SELECT rcpt.platform_id,
    rcpt.id AS receipt_id,
    rcpt.receiptnumber,
    rcpt.receipt_title,
    rcpt.receipt_date,
    rcpt.receipt_amount,
    rcpt.receipt_currency,
    rcpt.gratuity,
    rcpt.amountnet_reducedvat,
    rcpt.amountnet_fullvat,
    rcpt.vatrate_reduced,
    rcpt.vatrate_full,
    trcli.display_text ->> 'de'::text AS taxreason_client,
    mtaxr.taxation_type,
    mtaxr.taxation_type_display,
    mtaxr.payroll_staff,
    mtaxr.payroll_external,
    mtaxr.vat_deductible,
    mtaxr.opex_deductible,
    COALESCE(cb.rec_count, 0::bigint) AS beneficiary_count,
    COALESCE(cg.rec_count, 0::bigint) AS guest_count,
    COALESCE(cb.rec_count, 0::bigint) + COALESCE(cg.rec_count, 0::bigint) AS participant_count
   FROM receipts.receipts rcpt
     JOIN taxation.taxreason_client trcli ON trcli.id = rcpt.taxreason_client_id
     JOIN masterdata.taxation_nonevent mtaxr ON mtaxr.taxation_type = trcli.taxation_type_nonevent AND mtaxr.activeperiod @> rcpt.receipt_date
     JOIN receipts.receipt_approvals appr ON appr.receipt_id = rcpt.id
     LEFT JOIN count_beneficiary cb ON cb.receipt_id = rcpt.id
     LEFT JOIN count_guest cg ON cg.receipt_id = rcpt.id
  WHERE rcpt.taxation_run_id IS NULL AND appr.approval_withdrawn_at IS NULL AND appr.receipt_context = 'STANDALONE'::text;


----------------------------------------------------------------------------

Create view taxation.teamevents_unprocessed
	as select tev.platform_id,
	tev.id as teamevent_id,
	tev.projectnumber_id,
	tev.event_title,
	tev.event_date,
	tev.teameventtype_id,
	tet.apply_law40_2
	from events.team_event tev
	JOIN events.team_event_type tet on tet.id = tev.teameventtype_id
	left join events.team_event_to_guest tevguest ON tevguest.teamevent_id = tev.id
	join rexbase.guest guest ON guest.id = tevguest.guest_id
	left join events.team_event_to_beneficiary tevben ON tevben.teamevent_id = tev.id
	JOIN rexbase.beneficiary ben ON ben.id = tevben.beneficiary_id
	JOIN rexbase.beneficiary_orgdata benorg ON benorg.beneficiary_id = ben.id
	AND benorg.validperiod @> tev.event_date;
	

