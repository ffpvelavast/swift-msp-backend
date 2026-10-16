DROP FUNCTION IF EXISTS receipts.select_receipt_submitter(bigint);

CREATE OR REPLACE FUNCTION receipts.select_receipt_submitter(
	current_rexuser_id bigint)
    RETURNS TABLE(rexuser_id bigint, listname citext, fullname citext, receipt_owner boolean) 
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE 
    ROWS 1000

AS $BODY$

BEGIN
	RETURN QUERY
	-- returns the name of rexuser and his deputies for receipt submission  
	-- will be later extended to incorporate deputies
	SELECT 
		ru.id as rexuser_id,
		ru.listname, 
		ru.fullname,
		true as receipt_owner
	FROM rexbase.rexuser ru where ru.id=current_rexuser_id;
END;
$BODY$;