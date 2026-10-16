DROP FUNCTION expenses.select_expense_report(int8);

CREATE OR REPLACE FUNCTION expenses.select_expense_report(
    current_rexuser_id bigint
)
RETURNS TABLE(
    id bigint,
    rexuser_id bigint,
    report_title citext,
    reportdate_from date,
    reportdate_through date
)
LANGUAGE plpgsql
STABLE PARALLEL SAFE
AS $function$
BEGIN
    RETURN QUERY

    SELECT
        ex.id,
        ex.rexuser_id,
        ex.report_title,
        ex.reportdate_from,
        ex.reportdate_through
    FROM expenses.expense_reports ex
    WHERE ex.rexuser_id = current_rexuser_id;

END;
$function$;