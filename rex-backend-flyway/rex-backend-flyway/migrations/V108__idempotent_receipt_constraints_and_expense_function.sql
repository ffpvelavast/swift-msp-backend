DROP FUNCTION IF EXISTS expenses.select_expense_report(int8);

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

-- Drop only if exists
ALTER TABLE receipts.receipt_approvals
DROP CONSTRAINT IF EXISTS receipt_context_values;

-- Recreate constraint
ALTER TABLE receipts.receipt_approvals
    ADD CONSTRAINT receipt_context_values
        CHECK (
            receipt_context = ANY (
               ARRAY[
               'STANDALONE'::text,
               'TEAM_EVENT'::text,
               'CORPORATE_EVENT'::text
            ]
        )
    );