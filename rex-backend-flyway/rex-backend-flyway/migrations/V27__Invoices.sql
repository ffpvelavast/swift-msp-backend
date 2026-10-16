
DROP TABLE IF EXISTS invoices.invoice_beneficiary;
DROP TABLE IF EXISTS invoices.invoice_guest;

DROP TABLE IF EXISTS invoices.invoice_positions_beneficiary;
DROP TABLE IF EXISTS invoices.invoice_positions_guest;


DROP TABLE IF EXISTS invoices.invoice_to_corporate_event;
DROP TABLE IF EXISTS invoices.invoice_to_team_event;

DROP TABLE IF EXISTS invoices.invoice_history;
DROP TABLE IF EXISTS invoices.invoice_status;

DROP TABLE IF EXISTS invoices.invoice_responsibility;
DROP TABLE IF EXISTS invoices.invoice_positions;
DROP TABLE IF EXISTS invoices.invoices;

DROP TABLE IF EXISTS invoices.invoice_errors;
DROP TABLE IF EXISTS invoices.invoice_errorcodes;
DROP TABLE IF EXISTS invoices.invoice_payload;

---------------
Drop Sequence IF EXISTS invoices.rex_invoice_id_seq;
Drop Sequence IF EXISTS invoices.rex_invoice_position_id_seq;
Drop Sequence IF EXISTS invoices.invoice_errors_id_seq;
Drop Sequence IF EXISTS invoices.invoice_payload_id_seq;
Drop Sequence IF EXISTS invoices.invoice_responsibility_id_seq;
Drop Sequence IF EXISTS invoices.invoice_history_id_seq;

Drop Sequence IF EXISTS invoices.invoice_to_beneficiary_id_seq;
Drop Sequence IF EXISTS invoices.invoice_to_guest_id_seq;
Drop Sequence IF EXISTS invoices.invoice_positions_beneficiary_id_seq;
Drop Sequence IF EXISTS invoices.invoice_positions_guest_id_seq;


Create Sequence invoices.rex_invoice_id_seq;
Create Sequence invoices.rex_invoice_position_id_seq;
Create Sequence invoices.invoice_errors_id_seq;
Create Sequence invoices.invoice_payload_id_seq;
Create Sequence invoices.invoice_responsibility_id_seq;
Create Sequence invoices.invoice_history_id_seq;

Create Sequence invoices.invoice_to_beneficiary_id_seq;
Create Sequence invoices.invoice_to_guest_id_seq;
Create Sequence invoices.invoice_positions_beneficiary_id_seq;
Create Sequence invoices.invoice_positions_guest_id_seq;


---------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS invoices.invoices
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
    released_at timestamp(6) with time zone,
    id bigint NOT NULL DEFAULT nextval('invoices.rex_invoice_id_seq'::regclass),
    document_number text NOT NULL,
    document_link text,
    document_type text NOT NULL,
    document_name citext NOT NULL,
	document_date date,
    supply_date date,
    supply_month integer GENERATED ALWAYS AS (((EXTRACT(year FROM (supply_date)::timestamp without time zone) * (100)::numeric) + EXTRACT(month FROM (supply_date)::timestamp without time zone))) STORED,
    payment_date date,
    accounting_text citext,
    recipient_number text COLLATE pg_catalog."default",
    recipient citext NOT NULL,
	recipient_invoice_number text,
    vendor_number text COLLATE pg_catalog."default",
    vendor citext COLLATE pg_catalog."default" NOT NULL,
    vendor_invoice_number text COLLATE pg_catalog."default",
    invoicetotal_gross numeric(10,2),
    invoicetotal_net numeric(10,2),
    invoicetotal_vat numeric(10,2),
    currency character(5) COLLATE pg_catalog."default",
    payrolltaxtion_required boolean DEFAULT true,
    customfield_1 text COLLATE pg_catalog."default",
    customfield_2 text COLLATE pg_catalog."default",
    customfield_3 text COLLATE pg_catalog."default",
    customfield_4 text COLLATE pg_catalog."default",
    customfield_5 text COLLATE pg_catalog."default",
    customfield_6 text COLLATE pg_catalog."default",
    customfield_7 text COLLATE pg_catalog."default",
    customfield_8 text COLLATE pg_catalog."default",
    customfield_9 text COLLATE pg_catalog."default",
    customfield_10 text COLLATE pg_catalog."default",
    customfield_11 text COLLATE pg_catalog."default",
    customfield_12 text COLLATE pg_catalog."default",
    customfield_13 text COLLATE pg_catalog."default",
    customfield_14 text COLLATE pg_catalog."default",
    customfield_15 text COLLATE pg_catalog."default",
    customfield_16 text COLLATE pg_catalog."default",
    customfield_17 text COLLATE pg_catalog."default",
    customfield_18 text COLLATE pg_catalog."default",
    customfield_19 text COLLATE pg_catalog."default",
    customfield_20 text COLLATE pg_catalog."default",
    note text COLLATE pg_catalog."default",
	taxreason_client_id bigint,
	taxation_run_id bigint,
    CONSTRAINT invoice_pkey PRIMARY KEY (id),
	CONSTRAINT fk_invoice_taxation_run FOREIGN KEY (taxation_run_id)
		REFERENCES taxation.taxation_run (id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION,
	CONSTRAINT fk_invoice_taxreason_client FOREIGN KEY (taxreason_client_id)
		REFERENCES taxation.taxreason_client (id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS invoices.invoice_positions
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
    id bigint NOT NULL DEFAULT nextval('invoices.rex_invoice_position_id_seq'::regclass),
    invoice_id bigint NOT NULL,
    external_position_number text NOT NULL,
    position_line_number integer,
    factual_approver text NOT NULL,
    position_text text NOT NULL,
    projectnumber text,
    position_quantity integer,
    positionamount_gross numeric(10,2),
    positionamount_net numeric(10,2),
    positionamount_vat numeric(10,2),
    position_vat_rate integer,
    customfield_1 text,
    customfield_2 text,
    customfield_3 text,
    customfield_4 text,
    customfield_5 text,
    customfield_6 text,
    customfield_7 text,
    customfield_8 text,
    customfield_9 text,
    customfield_10 text,
    customfield_11 text,
    customfield_12 text,
    customfield_13 text,
    customfield_14 text,
    customfield_15 text,
    customfield_16 text,
    customfield_17 text,
    customfield_18 text,
    customfield_19 text,
    customfield_20 text,
	project_number_id bigint,
	taxreason_client_id bigint,
    CONSTRAINT invoice_position_pkey PRIMARY KEY (id),
	CONSTRAINT fk_invoice_projectnumber FOREIGN KEY (project_number_id)
		REFERENCES rexbase.projectnumbers (id) MATCH SIMPLE
		ON UPDATE NO ACTION
		ON DELETE NO ACTION,
	CONSTRAINT fk_invoice_positions_invoice FOREIGN KEY (invoice_id)
        REFERENCES invoices.invoices (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS invoices.invoice_responsibility
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    id bigint NOT NULL DEFAULT nextval('invoices.invoice_responsibility_id_seq'::regclass),
    invoice_id bigint NOT NULL,
    rexuser_id bigint,
    delegate_id bigint,
    delegated_at timestamp(6) with time zone,
    remarks text,
    responsible_from timestamp(6) with time zone NOT NULL DEFAULT '2020-01-01 00:00:00'::timestamp(6) without time zone,
    responsible_through timestamp(6) with time zone NOT NULL DEFAULT '2999-12-31 23:59:59'::timestamp(6) without time zone,
    responsible_period tstzrange GENERATED ALWAYS AS (tstzrange(responsible_from, responsible_through, '[)'::text)) STORED,
    is_active boolean DEFAULT true,
    CONSTRAINT invoice_responsibility_pkey PRIMARY KEY (id),
	CONSTRAINT fk_invoice_responsibility_rexuser FOREIGN KEY (rexuser_id)
		REFERENCES rexbase.rexuser (id) MATCH SIMPLE
		ON UPDATE NO ACTION
		ON DELETE NO ACTION,
	CONSTRAINT fk_invoice_responsibility_delegate FOREIGN KEY (delegate_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT responsibility_to_invoice_fk FOREIGN KEY (invoice_id)
        REFERENCES invoices.invoices (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

------------------------- Assign beneficiaries & guests ------------------------------

------------------------------- STATUS AND HISTORY -----------------------------------

CREATE TABLE IF NOT EXISTS invoices.invoice_status
(
	created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
    id bigint NOT NULL,
	sortid smallint NOT NULL default 1,
	invoice_status_enum TEXT NOT NULL,
	display_text JSONB NOT NULL,
	description JSONB NOT NULL,
	CONSTRAINT invoice_status_pkey PRIMARY KEY (id),
	CONSTRAINT invoice_status_values 
		CHECK (invoice_status_enum IN ('PENDING', 'DRAFT','RELEASED','PROCESSED','REJECTED','ERROR'))
);

CREATE TABLE IF NOT EXISTS invoices.invoice_history
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    id bigint NOT NULL DEFAULT nextval('invoices.invoice_history_id_seq'::regclass),
    invoice_id bigint NOT NULL,
    status_id bigint NOT NULL,
	active boolean NOT NULL DEFAULT FALSE,
	status_creator text NOT NULL DEFAULT 'SYSTEM',
    CONSTRAINT invoice_history_id PRIMARY KEY (id),
    CONSTRAINT fk_invoice_history_invoice FOREIGN KEY (invoice_id)
        REFERENCES invoices.invoices (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT fk_invoice_history_status FOREIGN KEY (status_id)
        REFERENCES invoices.invoice_status (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE UNIQUE INDEX one_active_invoice_history
ON invoices.invoice_history (invoice_id) WHERE active = true;


-------------------------- API RELATED TABLES ------------------------------------------
CREATE TABLE IF NOT EXISTS invoices.invoice_errorcodes
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT LOCALTIMESTAMP(6),
    id integer NOT NULL,
    invoice_error_de text NOT NULL,
    invoice_error_en text NOT NULL,
    CONSTRAINT invoice_errorcodes_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS invoices.invoice_errors
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT LOCALTIMESTAMP(6),
    id bigint NOT NULL DEFAULT nextval('invoices.invoice_errors_id_seq'::regclass),
    document_id text COLLATE pg_catalog."default",
    table_name character varying(50) COLLATE pg_catalog."default",
    field_name character varying(100) COLLATE pg_catalog."default",
    invoices_id bigint,
    verified_by character varying(255) COLLATE pg_catalog."default",
    rexuser_lookup_result character varying(50) COLLATE pg_catalog."default",
    projectnumber_input character varying(100) COLLATE pg_catalog."default",
    projectnumber_lookup_result character varying(50) COLLATE pg_catalog."default",
    error_message character varying(250) COLLATE pg_catalog."default",
    invoice_errorcode_id integer,
    CONSTRAINT invoice_errors_pkey PRIMARY KEY (id),
    CONSTRAINT fk_invoice_errors_invoice_errorcodes FOREIGN KEY (invoice_errorcode_id)
        REFERENCES invoices.invoice_errorcodes (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE IF NOT EXISTS invoices.invoice_payload
(
    created_at timestamp with time zone DEFAULT LOCALTIMESTAMP(6),
    id bigint NOT NULL DEFAULT nextval('invoices.invoice_payload_id_seq'::regclass),
    payload jsonb,
    CONSTRAINT invoice_payload_pkey PRIMARY KEY (id)
);

-------------------------------- Table invoice_to_corporate_event --------------------------

CREATE TABLE IF NOT EXISTS invoices.invoice_to_corporate_event
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
    invoice_id bigint NOT NULL,
	corporate_event_id bigint NOT NULL,
	CONSTRAINT invoice_to_corporateevent_pkey PRIMARY KEY (invoice_id,corporate_event_id),
	CONSTRAINT invoice_to_corporate_event_to_invoice FOREIGN KEY (invoice_id)
		REFERENCES invoices.invoices (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT invoice_to_corporate_event_to_corporate_event FOREIGN KEY (corporate_event_id)
		REFERENCES events.corporate_event (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-------------------------------- Table invoice_to_team_event --------------------------
CREATE TABLE IF NOT EXISTS invoices.invoice_to_team_event
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
    invoice_id bigint NOT NULL,
	team_event_id bigint NOT NULL,
	CONSTRAINT invoice_to_team_event_pkey PRIMARY KEY (invoice_id,team_event_id),
	CONSTRAINT invoice_to_team_event_to_invoice FOREIGN KEY (invoice_id)
		REFERENCES invoices.invoices (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT invoice_teamvent_team_event FOREIGN KEY (invoice_id)
		REFERENCES events.team_event (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

------------------------ Table invoice_to_benficiary and guest --------------------------


---======================= Assigning Invoices to Beneficiaries & Guests =============================
----------------------------------------- invoice level ---------------------------------------------

CREATE TABLE IF NOT EXISTS invoices.invoice_to_beneficiary
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    id bigint NOT NULL DEFAULT nextval('invoices.invoice_to_beneficiary_id_seq'::regclass),
    beneficiary_id bigint NOT NULL,
    invoice_id bigint NOT NULL,
    CONSTRAINT invoice_beneficiary_id_pkey PRIMARY KEY (id),
    CONSTRAINT fk_invoice_beneficiary_to_invoice FOREIGN KEY (invoice_id)
        REFERENCES invoices.invoices (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_invoice_beneficiary_to_beneficiary FOREIGN KEY (beneficiary_id)
        REFERENCES rexbase.beneficiary (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS invoices.invoice_to_guest
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    id bigint NOT NULL DEFAULT nextval('invoices.invoice_to_guest_id_seq'::regclass),
    guest_id bigint NOT NULL,
    invoice_id bigint NOT NULL,
    CONSTRAINT rex_invoice_guest_id_pkey PRIMARY KEY (id),
    CONSTRAINT fk_invoice_guest_to_invoice FOREIGN KEY (invoice_id)
        REFERENCES invoices.invoices (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_invoice_guest_to_guest FOREIGN KEY (guest_id)
        REFERENCES rexbase.guest (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

------------------------------- position level ---------------------------------------

CREATE TABLE IF NOT EXISTS invoices.invoice_positions_beneficiary
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
    id bigint NOT NULL DEFAULT nextval('invoices.invoice_positions_beneficiary_id_seq'::regclass),
    beneficiary_id bigint NOT NULL,
    invoice_position_id bigint NOT NULL,
    is_deleted boolean DEFAULT false,
    CONSTRAINT rex_invoice_position_beneficiary_id_pkey PRIMARY KEY (id),
    CONSTRAINT fk_invoice_position FOREIGN KEY (invoice_position_id)
        REFERENCES invoices.invoice_positions (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_invoice_positions_beneficiary_id FOREIGN KEY (beneficiary_id)
        REFERENCES rexbase.beneficiary (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE IF NOT EXISTS invoices.invoice_positions_guest
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
    id bigint NOT NULL DEFAULT nextval('invoices.invoice_positions_guest_id_seq'::regclass),
    guest_id bigint NOT NULL,
    invoice_position_id bigint NOT NULL,
    is_deleted boolean DEFAULT false,
    CONSTRAINT rex_invoice_positions_guest_id_pkey PRIMARY KEY (id),
    CONSTRAINT fk_invoice_positions FOREIGN KEY (invoice_position_id)
        REFERENCES invoices.invoice_positions (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_invoice_position_guest_id FOREIGN KEY (guest_id)
        REFERENCES rexbase.guest (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
