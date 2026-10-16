-- 1. Drop old constraint
ALTER TABLE receipts.receipt_approvals
DROP CONSTRAINT receipt_context_values;

-- 2. Add corrected constraint
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