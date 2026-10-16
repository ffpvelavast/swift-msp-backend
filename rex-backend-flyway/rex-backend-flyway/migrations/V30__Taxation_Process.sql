DROP TABLE IF EXISTS taxation.receipts_standalone_reporting;
DROP TABLE IF EXISTS taxation.receipts_standalone_accounting;
DROP TABLE IF EXISTS taxation.receipts_standalone_payroll;


-------------------------------------------------------------------------------------------------------
------------------ taxation of standalone receipts ----------------------------------------------------
DROP TABLE IF EXISTS taxation.receipts_standalone_reporting;
Drop Sequence IF EXISTS taxation.receipts_standalone_reporting_id_seq;
Create Sequence taxation.receipts_standalone_reporting_id_seq;


CREATE TABLE IF NOT EXISTS taxation.receipts_standalone_reporting
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    id bigint NOT NULL DEFAULT nextval('taxation.receipts_standalone_reporting_id_seq'::regclass),
	receipt_id bigint NOT NULL,
    taxation_run_id bigint NOT NULL,
	legal_description text NOT NULL,
    CONSTRAINT taxation_standalone_reporting_id PRIMARY KEY (id),
	CONSTRAINT fk_taxation_standalone_reporting_taxation_run FOREIGN KEY (taxation_run_id)
		REFERENCES taxation.taxation_run (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT fk_taxation_standalone_reporting_to_receipt FOREIGN KEY (receipt_id)
        REFERENCES receipts.receipts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

--- CREATE TABLE IF NOT EXISTS taxation.receipts_standalone_payroll
--- CREATE TABLE IF NOT EXISTS taxation.receipts_standalone_accounting