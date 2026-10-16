DROP FUNCTION IF EXISTS expenses.select_expense_report(bigint);

CREATE OR REPLACE FUNCTION expenses.select_expense_report(current_rexuser_id bigint)
    RETURNS TABLE(rexuser_id bigint, report_title citext, reportdate_from date, reportdate_through date ) 
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE 
    ROWS 1000

AS $BODY$
BEGIN
	RETURN QUERY

	SELECT 
		ex.rexuser_id,
		ex.report_title,
		ex.reportdate_from,
		ex.reportdate_through
		FROM expenses.expense_reports ex
		Where ex.rexuser_id=current_rexuser_id;

END;
$BODY$;