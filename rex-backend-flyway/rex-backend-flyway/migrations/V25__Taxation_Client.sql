DROP TABLE IF EXISTS taxation.taxreason_client;
DROP TABLE IF EXISTS taxation.taxcategory_client;
DROP TABLE IF EXISTS masterdata.taxation_nonevent;


---------------------------------------------------------------------------
DROP SEQUENCE IF EXISTS masterdata.taxation_nonevent_id_seq;
DROP SEQUENCE IF EXISTS taxation.taxcategory_client_id_seq;
DROP SEQUENCE IF EXISTS taxation.taxreason_client_id_seq;

CREATE SEQUENCE masterdata.taxation_nonevent_id_seq;
CREATE SEQUENCE taxation.taxcategory_client_id_seq;
CREATE SEQUENCE taxation.taxreason_client_id_seq;

--------------------- controlling table for taxation -----------------------

CREATE TABLE IF NOT EXISTS masterdata.taxation_nonevent
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval('masterdata.taxation_nonevent_id_seq'::regclass),
	taxation_type text NOT NULL,
	taxation_type_display text NOT NULL,
	taxation_value_dependant boolean NOT NULL DEFAULT FALSE,
	payroll_staff text NOT NULL DEFAULT 'NO',
	payroll_external text NOT NULL DEFAULT 'NO',
	vat_deductible text NOT NULL DEFAULT 'NO',
	opex_deductible text NOT NULL DEFAULT 'NO',
	remarks text,
	activefrom date NOT NULL default '2020-01-01'::date,
	activethrough date NOT NULL default '2999-12-31'::date,
	activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, activethrough+1,'[)')) STORED,
    CONSTRAINT taxation_rules_pkey PRIMARY KEY (id),
	CONSTRAINT taxation_type_value
		CHECK (taxation_type IN ('HOSPITALITY_EXTERNAL', 'HOSPITALITY_INTERNAL',
							    'GIFT_EXTERNAL_WITH_OCCASION','GIFT_EXTERNAL_NO_OCCASION',
								'GIFT_EMPLOYEE_WITH_OCCASION','GIFT_EMPLOYEE_NO_OCCASION',
								'ANNIVERSARY_GIFT','ANNIVERSARY_SINGLE_PERSON','ANNIVERSARY_MULTIPLE_PERSON')),	
	CONSTRAINT payroll_staff_value
		CHECK (payroll_staff IN ('YES', 'NO','VALUE_DEPENDANT','NOT_APPLICABLE')),	
	CONSTRAINT payroll_external_value
		CHECK (payroll_external IN ('YES', 'NO','VALUE_DEPENDANT','NOT_APPLICABLE')),	
	CONSTRAINT vat_deductible_value
		CHECK (vat_deductible IN ('YES', 'NO','VALUE_DEPENDANT')),	
	CONSTRAINT opex_deductible_value
		CHECK (opex_deductible IN ('YES', 'NO','VALUE_DEPENDANT','RATIO_70')),
	CONSTRAINT taxation_rules_validity EXCLUDE USING gist (taxation_type with=,activeperiod WITH &&)
);

--------------------- Client's tables for taxation -----------------------

CREATE TABLE IF NOT EXISTS taxation.taxcategory_client
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval( 'taxation.taxcategory_client_id_seq'::regclass),
	sortid smallint NOT NULL Default 1,
    platform_id bigint NOT NULL,
	display_text JSONB NOT NULL,
	help_text JSONB,
	description JSONB,
	eligible_invoice BOOLEAN NOT NULL default TRUE,
	eligible_receipt BOOLEAN NOT NULL default TRUE,
	activefrom date NOT NULL default '2020-01-01'::date,
	activethrough date NOT NULL default '2999-12-31'::date,
	activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, activethrough+1,'[)')) STORED,
    CONSTRAINT taxcategory_client_pkey PRIMARY KEY (id),
	CONSTRAINT taxcategory_client_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS taxation.taxreason_client
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval( 'taxation.taxreason_client_id_seq'::regclass),
	sortid smallint NOT NULL Default 1,
    platform_id bigint NOT NULL,
    taxcategory_client_id bigint NOT NULL,
	taxation_type_nonevent text NOT NULL,
	display_text JSONB NOT NULL,
	help_text JSONB,
	description JSONB,
	eligible_invoice BOOLEAN NOT NULL default TRUE,
	eligible_receipt BOOLEAN NOT NULL default TRUE,
	permitted_value decimal (8,2),
	activefrom date NOT NULL default '2020-01-01'::date,
	activethrough date NOT NULL default '2999-12-31'::date,
	activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, activethrough+1,'[)')) STORED,
    CONSTRAINT taxreason_client_pkey PRIMARY KEY (id),
	CONSTRAINT taxreason_client_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT taxreason_client_to_taxcategory_client FOREIGN KEY (taxcategory_client_id)
        REFERENCES taxation.taxcategory_client (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


