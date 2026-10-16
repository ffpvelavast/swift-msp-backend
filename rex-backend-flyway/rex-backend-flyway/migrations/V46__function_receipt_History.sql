DROP FUNCTION IF EXISTS receipts.receipt_history(bigint, char(2));

CREATE OR REPLACE FUNCTION receipts.receipt_history(current_receipt_id bigint,user_locale char(2))
    RETURNS TABLE(
		created_at timestamp with time zone,  
		listname citext,
		lastname citext,
		firstname citext,
		external_note text,
		active boolean,
		receipt_status text) 
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE 
    ROWS 1000

AS $BODY$

BEGIN
	RETURN QUERY
	
	SELECT 
		rh.created_at,
		rx.listname,
		rx.lastname,
		rx.firstname,
		rh.external_note,
		rh.active,
		rs.display_text ->> lower(user_locale)
		FROM receipts.receipt_history rh
		JOIN receipts.receipt_status rs ON rs.id = rh.receiptstatus_id
		JOIN rexbase.rexuser rx ON rx.id = rh.creator_id
		Where rh.receipt_id=current_receipt_id
		ORDER BY rh.created_at;
END;
$BODY$;

