DROP TABLE IF EXISTS rexbase.employer_accounting;
DROP TABLE IF EXISTS rexbase.employer_payroll;
DROP TABLE IF EXISTS rexbase.employer;

DROP SEQUENCE IF EXISTS rexbase.employer_id_seq;
DROP SEQUENCE IF EXISTS rexbase.employer_accounting_id_seq;
DROP SEQUENCE IF EXISTS rexbase.employer_payroll_id_seq;

CREATE SEQUENCE rexbase.employer_id_seq;
CREATE SEQUENCE rexbase.employer_accounting_id_seq;
CREATE SEQUENCE rexbase.employer_payroll_id_seq;


--------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS rexbase.employer
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval('rexbase.employer_id_seq'::regclass),
    employer_uuid uuid NOT NULL,
    displayname citext COLLATE pg_catalog."default" NOT NULL,
    platform_id bigint NOT NULL,
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT employer_pkey PRIMARY KEY (id),
    CONSTRAINT uq_employer_employer_uuid UNIQUE (employer_uuid),
    CONSTRAINT fk_employer_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT employer_validity EXCLUDE USING gist (id WITH =,activeperiod WITH &&)
);

CREATE TABLE IF NOT EXISTS rexbase.employer_accounting
(	created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	id bigint NOT NULL DEFAULT nextval('rexbase.employer_accounting_id_seq'::regclass),
	employer_id bigint NOT NULL,
    platform_id bigint NOT NULL,
	companyid_accounting text NOT NULL,
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT employer_accounting_pkey PRIMARY KEY (id),
	CONSTRAINT fk_employer_accounting_employer FOREIGN KEY (employer_id)
	    REFERENCES rexbase.employer (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT fk_employer_accounting_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT employer_accounting_validity EXCLUDE USING gist (id WITH =,activeperiod WITH &&)
);

CREATE TABLE IF NOT EXISTS rexbase.employer_payroll
(	created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	id bigint NOT NULL DEFAULT nextval('rexbase.employer_payroll_id_seq'::regclass),
	employer_id bigint NOT NULL,
    platform_id bigint NOT NULL,
	companyid_payroll text NOT NULL,
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT employer_payroll_pkey PRIMARY KEY (id),
	CONSTRAINT fk_employer_payroll_employer FOREIGN KEY (employer_id)
	    REFERENCES rexbase.employer (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT fk_employer_payroll_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT employer_payroll_validity EXCLUDE USING gist (id WITH =,activeperiod WITH &&)
);

