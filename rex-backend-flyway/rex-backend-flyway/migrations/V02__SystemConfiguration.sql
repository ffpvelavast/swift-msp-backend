------------------ Create the system configuration -------------
DROP TABLE IF EXISTS masterdata.system_configuration;
DROP TABLE IF EXISTS masterdata.taxation_nonevent;
DROP TABLE IF EXISTS masterdata.taxation_thresholds;


DROP SEQUENCE IF EXISTS masterdata.system_configuration_id_seq;
DROP SEQUENCE IF EXISTS masterdata.taxation_nonevent_id_seq;
DROP SEQUENCE IF EXISTS masterdata.taxation_thresholds_id_seq;


CREATE SEQUENCE masterdata.system_configuration_id_seq;
CREATE SEQUENCE masterdata.taxation_nonevent_id_seq;
CREATE SEQUENCE masterdata.taxation_thresholds_id_seq;


-----------------------------------------------------------------


CREATE TABLE IF NOT EXISTS masterdata.system_configuration
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    id bigint NOT NULL DEFAULT nextval('masterdata.system_configuration_id_seq'::regclass),
	vatrate_reduced smallint not NULL,
	vatrate_full smallint not NULL,
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT system_configuration_pkey PRIMARY KEY (id),
	CONSTRAINT system_configuration_date_check CHECK (activefrom <= activethrough),
    CONSTRAINT system_configuration_validity EXCLUDE USING gist (activeperiod WITH &&)
);

---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS masterdata.taxation_nonevent
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval('masterdata.taxation_nonevent_id_seq'::regclass),
    taxation_type text COLLATE pg_catalog."default" NOT NULL,
    taxation_type_display text COLLATE pg_catalog."default" NOT NULL,
    payroll_staff text COLLATE pg_catalog."default" NOT NULL DEFAULT 'NO'::text,
    payroll_external text COLLATE pg_catalog."default" NOT NULL DEFAULT 'NO'::text,
    vat_deductible text COLLATE pg_catalog."default" NOT NULL DEFAULT 'NO'::text,
    opex_deductible text COLLATE pg_catalog."default" NOT NULL DEFAULT 'NO'::text,
    remarks text COLLATE pg_catalog."default",
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT taxation_rules_pkey PRIMARY KEY (id),
    CONSTRAINT opex_deductible_value CHECK (opex_deductible = ANY (ARRAY['YES'::text, 'NO'::text, 'VALUE_DEPENDANT'::text, 'RATIO_70'::text])),
    CONSTRAINT payroll_external_value CHECK (payroll_external = ANY (ARRAY['YES'::text, 'NO'::text, 'VALUE_DEPENDANT'::text, 'NOT_APPLICABLE'::text])),
    CONSTRAINT payroll_staff_value CHECK (payroll_staff = ANY (ARRAY['YES'::text, 'NO'::text, 'VALUE_DEPENDANT'::text, 'NOT_APPLICABLE'::text])),
    CONSTRAINT taxation_type_value CHECK (taxation_type = ANY (ARRAY['HOSPITALITY_EXTERNAL'::text, 'HOSPITALITY_INTERNAL'::text, 'HOSPITALITY_OTHER'::text, 'GIFT_EXTERNAL_WITH_OCCASION'::text, 'GIFT_EXTERNAL_NO_OCCASION'::text, 'GIFT_EMPLOYEE_WITH_OCCASION'::text, 'GIFT_EMPLOYEE_NO_OCCASION'::text, 'ANNIVERSARY_GIFT'::text, 'ANNIVERSARY_SINGLE_PERSON'::text, 'ANNIVERSARY_MULTIPLE_PERSON'::text])),
    CONSTRAINT vat_deductible_value CHECK (vat_deductible = ANY (ARRAY['YES'::text, 'NO'::text, 'VALUE_DEPENDANT'::text])),
    CONSTRAINT taxation_rules_validity EXCLUDE USING gist (
        taxation_type WITH =,
        activeperiod WITH &&)

);

---------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS masterdata.taxation_thresholds
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	id bigint NOT NULL DEFAULT nextval('masterdata.taxation_thresholds_id_seq'::regclass),
    threshold_type text NOT NULL,
    threshold_value decimal(8,2) not null,
    remarks text,
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT taxation_thresholds_pkey PRIMARY KEY (id),
    CONSTRAINT taxation_thresholds_validity EXCLUDE USING gist (
        threshold_type WITH =,
        activeperiod WITH &&)
);

--------------------------------------------------------------------------------








