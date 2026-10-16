DROP TABLE IF EXISTS rexbase.platform_keycloakclients;
DROP TABLE IF EXISTS rexbase.platform_taxation;
DROP TABLE IF EXISTS rexbase.platform_configuration;
DROP TABLE IF EXISTS rexbase.platform_domain;
DROP TABLE IF EXISTS rexbase.platform;

DROP SEQUENCE IF EXISTS rexbase.platform_id_seq;
DROP SEQUENCE IF EXISTS rexbase.platform_domain_id_seq;
DROP SEQUENCE IF EXISTS rexbase.platform_configuration_id_seq;
DROP SEQUENCE IF EXISTS rexbase.platform_taxation_id_seq;
DROP SEQUENCE IF EXISTS rexbase.platform_keycloakclients_id_seq;

CREATE SEQUENCE rexbase.platform_id_seq;
CREATE SEQUENCE rexbase.platform_domain_id_seq;
CREATE SEQUENCE rexbase.platform_configuration_id_seq;
CREATE SEQUENCE rexbase.platform_taxation_id_seq;
CREATE SEQUENCE rexbase.platform_keycloakclients_id_seq;


---------- ============================================================= -------------

CREATE TABLE IF NOT EXISTS rexbase.platform
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
    id bigint NOT NULL DEFAULT nextval('rexbase.platform_id_seq'::regclass),
    platform_uuid uuid NOT NULL,
    displayname citext NOT NULL,
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT platform_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS rexbase.platform_domain
(
    id bigint NOT NULL DEFAULT nextval('rexbase.platform_domain_id_seq'::regclass),
    platform_id bigint NOT NULL,
    domainname citext COLLATE pg_catalog."default",
    CONSTRAINT platform_domain_pkey PRIMARY KEY (id),
    CONSTRAINT domainname_ukey UNIQUE (domainname),
    CONSTRAINT uq_platform_domain_name UNIQUE (domainname),
    CONSTRAINT fk_platform_domain_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_platform_domain_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS rexbase.platform_configuration
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    id bigint NOT NULL DEFAULT nextval('rexbase.platform_configuration_id_seq'::regclass),
    platform_id bigint NOT NULL,
	platform_url text,
    invoice_storage text NOT NULL DEFAULT 'REX_SYSTEM',
    invoice_storage_link text,
    receipt_storage text NOT NULL DEFAULT 'REX_SYSTEM',
    receipt_storage_link text,
    keycloak_url text,
    keycloak_realm text,
	projectnumber_required_invoices boolean default true,
	projectnumber_required_receipts boolean default true,
	corporate_event_preview smallint not NULL default 3,
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT platform_configuration_pkey PRIMARY KEY (id, activefrom),
    CONSTRAINT platform_configuration_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT invoice_storage_values 
		CHECK (invoice_storage IN ('CLIENT_SYSTEM', 'REX_SYSTEM')),
	CONSTRAINT receipt_storage_values 
		CHECK (receipt_storage IN ('CLIENT_SYSTEM', 'REX_SYSTEM')),
    CONSTRAINT platform_config_validity EXCLUDE USING gist (platform_id WITH =,activeperiod WITH &&)
);



CREATE TABLE IF NOT EXISTS rexbase.platform_taxation
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
    id bigint NOT NULL DEFAULT nextval('rexbase.platform_taxation_id_seq'::regclass),
	platform_id bigint NOT NULL,
	event_type_selection boolean NOT NULL default false,
	use_law37a boolean NOT NULL default false,
	use_law37b boolean NOT NULL default false,
	use_law40_2 boolean NOT NULL default true,
	activefrom date NOT NULL default '2020-01-01'::date,
	activethrough date NOT NULL default '2999-12-31'::date,
	activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, activethrough+1,'[)')) STORED,
    CONSTRAINT platform_taxation_pkey PRIMARY KEY (id),
	CONSTRAINT platform_taxation_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT platform_taxation_validity EXCLUDE USING gist (platform_id WITH =,activeperiod WITH &&)
);

CREATE TABLE IF NOT EXISTS rexbase.platform_keycloakclients
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
	id bigint NOT NULL DEFAULT nextval('rexbase.platform_keycloakclients_id_seq'::regclass),
	platform_id bigint NOT NULL,
	clientid text NOT NULL,
	client_name text NOT NULL,
	--- client_type: TECHNICAL, FRONTEND ---
	client_type text NULL, 
	client_description text,
	activefrom date NOT NULL default '2020-01-01'::date,
	activethrough date NOT NULL default '2999-12-31'::date,
	activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, activethrough+1,'[)')) STORED,
    CONSTRAINT platform_keycloakclient_pkey PRIMARY KEY (id,activefrom),
	CONSTRAINT platform_keycloakclient_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


