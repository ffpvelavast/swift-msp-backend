DROP TABLE IF EXISTS rexbase.beneficiary_orgdata;
DROP TABLE IF EXISTS rexbase.beneficiary;

DROP SEQUENCE IF EXISTS rexbase.beneficiary_id_seq;
DROP SEQUENCE IF EXISTS rexbase.beneficiary_orgdata_id_seq;

CREATE SEQUENCE rexbase.beneficiary_id_seq;
CREATE SEQUENCE rexbase.beneficiary_orgdata_id_seq;

------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS rexbase.beneficiary
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval('rexbase.beneficiary_id_seq'::regclass),
	platform_id bigint,
    commodisid uuid NOT NULL,
    beneficiaryid uuid NOT NULL,
	externalsystemid text COLLATE pg_catalog."default",
    lastname citext NOT NULL,
    firstname citext COLLATE pg_catalog."default" NOT NULL,
    academictitle citext COLLATE pg_catalog."default",
	listname citext COLLATE pg_catalog."default" GENERATED ALWAYS AS (TRIM(BOTH FROM ((COALESCE(lastname, '*unbekannt*'::text) || ', '::text) || btrim(((COALESCE((academictitle)::text, ''::text) || ' '::text) || COALESCE((firstname)::text, '*unbekannt*'::text)))))) STORED,
	fullname citext GENERATED ALWAYS AS (btrim(COALESCE(academictitle,'') || ' ' || COALESCE(firstname, '*unbekannt*') ||  ' ' || COALESCE(lastname,'*unbekannt*'))) STORED,
    gender character(1) COLLATE pg_catalog."default",
    emailaddress citext COLLATE pg_catalog."default",
	leavingdate date,
    rexuser_id bigint,
    CONSTRAINT beneficiary_pkey PRIMARY KEY (id),
    CONSTRAINT uq_beneficiary_beneficiaryid UNIQUE (beneficiaryid),
    CONSTRAINT uq_beneficiary_commodisid UNIQUE (commodisid),
    CONSTRAINT fk_beneficiary_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS rexbase.beneficiary_orgdata
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	id bigint NOT NULL DEFAULT nextval('rexbase.beneficiary_orgdata_id_seq'::regclass),
    beneficiary_id bigint NOT NULL,
    employer_id bigint NOT NULL,
	companyid_payroll text COLLATE pg_catalog."default",
    companyid_accounting text COLLATE pg_catalog."default",
    personnelnumber_payroll text COLLATE pg_catalog."default",
    personnelnumber_system text COLLATE pg_catalog."default",
	personnelnumber_accounting text COLLATE pg_catalog."default",
    companysite citext COLLATE pg_catalog."default",
    costcenter citext COLLATE pg_catalog."default",
    identity_tag citext COLLATE pg_catalog."default",
    orgunit text COLLATE pg_catalog."default",
    orgunit_above text COLLATE pg_catalog."default",
	manager boolean NOT NULL DEFAULT false,
    active boolean NOT NULL DEFAULT false,
	validfrom date NOT NULL DEFAULT '2020-01-01'::date,
    validthrough date NOT NULL DEFAULT '2999-12-31'::date,
    validperiod daterange GENERATED ALWAYS AS (daterange(validfrom, (validthrough + 1), '[)'::text)) STORED,
    CONSTRAINT beneficiary_orgdata_pkey PRIMARY KEY (id),
    CONSTRAINT fk_beneficiary_orgdata_beneficiary FOREIGN KEY (beneficiary_id)
        REFERENCES rexbase.beneficiary (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT fk_beneficiary_orgdata_to_employer FOREIGN KEY (employer_id)
        REFERENCES rexbase.employer (id) MATCH SIMPLE
        ON UPDATE NO ACTION	
        ON DELETE NO ACTION,
    CONSTRAINT beneficiary_orgdata_validity EXCLUDE USING gist (
        beneficiary_id WITH =, validperiod WITH &&)
);

