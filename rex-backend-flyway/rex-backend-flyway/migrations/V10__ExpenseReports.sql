DROP TABLE IF EXISTS expenses.expense_reports;


DROP SEQUENCE IF EXISTS expenses.expense_reports_id_seq;

CREATE SEQUENCE expenses.expense_reports_id_seq;



---------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS expenses.expense_reports
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval('expenses.expense_reports_id_seq'::regclass),
    rexuser_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    report_title citext NOT NULL,
    reportdate_from date NOT NULL,
    reportdate_through date NOT NULL,
    CONSTRAINT expense_report_pkey PRIMARY KEY (id),
    CONSTRAINT expense_report_to_creator FOREIGN KEY (creator_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT expense_report_to_rexuser FOREIGN KEY (rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);