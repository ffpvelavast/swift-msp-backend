--------------------------------------------------------------------
--------------------- receipts -------------------------------------
--------------------------------------------------------------------

DROP TABLE IF EXISTS receipts.receipt_to_beneficiary;
DROP TABLE IF EXISTS receipts.receipt_to_guest;

DROP TABLE IF EXISTS receipts.receipt_to_corporateevent;
DROP TABLE IF EXISTS receipts.receipt_to_teamevent;

DROP TABLE IF EXISTS receipts.receipt_files;

DROP TABLE IF EXISTS receipts.receipt_approvals;
DROP TABLE IF EXISTS receipts.receipt_approver;
DROP TABLE IF EXISTS receipts.receipt_history;
DROP TABLE IF EXISTS receipts.receipt_status;
DROP TABLE IF EXISTS receipts.receipts;

DROP TABLE IF EXISTS receipts.issuance_region;
DROP TABLE IF EXISTS receipts.filetypes;

---------------------------------------------------------------------------------------

DROP SEQUENCE IF EXISTS receipts.receipt_id_seq;
DROP SEQUENCE IF EXISTS receipts.receipt_history_id_seq;
DROP SEQUENCE IF EXISTS receipts.receipt_files_id_seq;

CREATE SEQUENCE receipts.receipt_id_seq;
CREATE SEQUENCE receipts.receipt_history_id_seq;
CREATE SEQUENCE receipts.receipt_files_id_seq;

---------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS receipts.receipt_status
(
	created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    id smallint NOT NULL,
	display_text JSONB NOT NULL,
	status_code varchar(50) NOT NULL,
	CONSTRAINT receipt_status_pkey PRIMARY KEY (id),
	CONSTRAINT receipt_status_code_check CHECK (((status_code)::text = ANY 
	((ARRAY['SUBMITTED'::character varying, 'CORRECTION_REQUESTED'::character varying, 'RESUBMITTED'::character varying, 
	'REJECTED'::character varying, 'APPROVED'::character varying, 'WITHDRAWN'::character varying,'PROCESSED'::character varying])::text[]))),
	CONSTRAINT receipt_status_code_unique UNIQUE (status_code)
);

----------------------- receipt receipt_issuance ---------------------------------------

CREATE TABLE IF NOT EXISTS receipts.issuance_region
(
	created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    id smallint NOT NULL,
	region_code text NOT NULL,
	display_text JSONB NOT NULL,
	description JSONB,
	CONSTRAINT issuance_region_pkey PRIMARY KEY (id)
);

---------------------- receipt filetypes ------------------------------------------------

CREATE TABLE IF NOT EXISTS receipts.filetypes
(
	created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    id smallint NOT NULL,
	filetype_code varchar(50) NOT NULL,
	display_text JSONB NOT NULL,
	CONSTRAINT filetypes_pkey PRIMARY KEY (id)
);

----------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS receipts.receipts
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval( 'receipts.receipt_id_seq'::regclass),
	platform_id bigint NOT NULL,
	processing_released_at timestamp(6) with time zone,
	processing_released_by text,
	receiptnumber text NOT NULL,
	rexuser_id bigint NOT NULL,
	expensereport_id bigint NOT NULL,
	issuance_region_id smallint NOT NULL,
	receipt_title citext NOT NULL,
	receipt_date date NOT NULL,
	receipt_amount decimal(8,2) NOT NULL,
	receipt_currency char(3) Not NULL default 'EUR',
	gratuity decimal(8,2),
	amountnet_reducedVAT decimal(8,2),
	amountnet_fullVAT decimal(8,2),
	vatrate_reduced smallint,
	vatrate_full smallint,
	internal_note text,
	projectnumber_id bigint,
	deduct_team_balance boolean NOT NULL DEFAULT false,
	taxreason_client_id bigint,
	taxation_run_id bigint,
	CONSTRAINT receipt_pkey PRIMARY KEY (id),
	CONSTRAINT receipts_receiptnumber_uq UNIQUE (receiptnumber),
	CONSTRAINT receipt_to_rexuser FOREIGN KEY (rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT receipts_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION,
	CONSTRAINT receipt_to_expensereport FOREIGN KEY (expensereport_id)
        REFERENCES expenses.expense_reports (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT receipt_to_projectnumber FOREIGN KEY (projectnumber_id)
        REFERENCES rexbase.projectnumbers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT receipt_to_issuance_region FOREIGN KEY (issuance_region_id)
        REFERENCES receipts.issuance_region (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT fk_receipt_taxation_run FOREIGN KEY (taxation_run_id)
		REFERENCES taxation.taxation_run (id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION,
	CONSTRAINT fk_receipt_taxreason_client FOREIGN KEY (taxreason_client_id)
		REFERENCES taxation.taxreason_client (id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION
);


-----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS receipts.receipt_approvals
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	receipt_id bigint NOT NULL,
	approver_id bigint NOT NULL,
	receipt_context text NOT NULL,
	approval_withdrawn_at timestamp(6) with time zone,
	withdrawer_id bigint,
	CONSTRAINT receipt_approvals_pkey PRIMARY KEY (receipt_id,approver_id),
	CONSTRAINT fk_receipt_approvals_to_receipt FOREIGN KEY (receipt_id)
        REFERENCES receipts.receipts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT fk_receipt_approvals_to_rexuser FOREIGN KEY (approver_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT fk_receipt_approval_withdrawal_to_rexuser FOREIGN KEY (withdrawer_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT receipt_context_values 
		CHECK (receipt_context IN ('STANDALONE', 'TEAM_EVENT','CORPRATE_EVENT'))
);


CREATE TABLE IF NOT EXISTS receipts.receipt_approver
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	receipt_id bigint NOT NULL,
	rexuser_id bigint NOT NULL,
	CONSTRAINT receipt_approver_pkey PRIMARY KEY (receipt_id,rexuser_id),
	CONSTRAINT receipt_approver_to_receipt FOREIGN KEY (receipt_id)
        REFERENCES receipts.receipts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT receipt_approver_to_rexuser FOREIGN KEY (rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE IF NOT EXISTS receipts.receipt_history
(
	created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	id bigint NOT NULL DEFAULT nextval( 'receipts.receipt_history_id_seq'::regclass),
	receipt_id bigint NOT NULL,
	creator_id bigint not NULL,
	receiptstatus_id smallint NOT NULL,
	external_note text,
	active BOOLEAN NOT NULL DEFAULT FALSE,
	CONSTRAINT receipt_history_pkey PRIMARY KEY (id),
	CONSTRAINT receipt_history_to_receipt FOREIGN KEY (receipt_id)
        REFERENCES receipts.receipts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT receipt_history_to_rexuser FOREIGN KEY (creator_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT receipt_history_to_receipt_status FOREIGN KEY (receiptstatus_id)
        REFERENCES receipts.receipt_status (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

--- DROP INDEX IF EXISTS one_active_receipt_history;

CREATE UNIQUE INDEX one_active_receipt_history
ON receipts.receipt_history (receipt_id) WHERE active = true;

----------------------- receipt files --------------------------------------

CREATE TABLE IF NOT EXISTS receipts.receipt_files
(
	created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval( 'receipts.receipt_files_id_seq'::regclass),
	receipt_id bigint NOT NULL,
	receiptfileid UUID NOT NULL,
	receiptnumber_seq INTEGER NOT NULL, 
	filetype_id smallint NOT NULL default 1,
	filename text NOT NULL,
	original_filename text NOT NULL,
	mediatype text not NULL,
	CONSTRAINT receiptfile_pkey PRIMARY KEY (id),
	CONSTRAINT receipt_files_to_receipt FOREIGN KEY (receipt_id)
        REFERENCES receipts.receipts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT receiptfile_to_filetype FOREIGN KEY (filetype_id)
        REFERENCES receipts.filetypes (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


-------------------------------- Table receipt_to_corporate_event --------------------------

CREATE TABLE IF NOT EXISTS receipts.receipt_to_corporateevent
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
    receipt_id bigint NOT NULL,
	corporate_event_id bigint NOT NULL,
	CONSTRAINT receipt_to_corporatevent_pkey PRIMARY KEY (receipt_id,corporate_event_id),
	CONSTRAINT receipt_to_corporatevent_to_receipts FOREIGN KEY (receipt_id)
		REFERENCES receipts.receipts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT receipt_to_corporateevent_to_corporateevent FOREIGN KEY (corporate_event_id)
		REFERENCES events.corporate_event (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE INDEX idx_receipt_to_corporateevent ON receipts.receipt_to_corporateevent (receipt_id);


-------------------------------- Table receipt_to_team_event --------------------------
CREATE TABLE IF NOT EXISTS receipts.receipt_to_teamevent
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
    receipt_id bigint NOT NULL,
	teamevent_id bigint NOT NULL,
	CONSTRAINT receipt_to_teamevent_pkey PRIMARY KEY (receipt_id,teamevent_id),
	CONSTRAINT receipt_to_teamevent_to_receipt FOREIGN KEY (receipt_id)
		REFERENCES receipts.receipts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT receipt_to_teamevent_to_teamevent FOREIGN KEY (teamevent_id)
		REFERENCES events.team_event (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE INDEX idx_receipt_to_teamevent ON receipts.receipt_to_teamevent (receipt_id);


--------------

CREATE TABLE IF NOT EXISTS receipts.receipt_to_beneficiary
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
    receipt_id bigint NOT NULL,
	beneficiary_id bigint NOT NULL,
	CONSTRAINT receipt_to_beneficiary_pkey PRIMARY KEY (receipt_id,beneficiary_id),
	CONSTRAINT receipt_to_beneficiary_to_receipts FOREIGN KEY (receipt_id)
		REFERENCES receipts.receipts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT receipt_to_beneficiary_beneficiary FOREIGN KEY (beneficiary_id)
		REFERENCES rexbase.beneficiary (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE INDEX idx_receipt_to_beneficiary ON receipts.receipt_to_beneficiary (receipt_id);


CREATE TABLE IF NOT EXISTS receipts.receipt_to_guest
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
    receipt_id bigint NOT NULL,
	guest_id bigint NOT NULL,
	CONSTRAINT receipt_to_guest_pkey PRIMARY KEY (receipt_id,guest_id),
	CONSTRAINT receipt_to_guest_to_receipt FOREIGN KEY (receipt_id)
		REFERENCES receipts.receipts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT receipt_to_guest_guest FOREIGN KEY (guest_id)
		REFERENCES rexbase.guest (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE INDEX idx_receipt_to_guest ON receipts.receipt_to_guest (receipt_id);
