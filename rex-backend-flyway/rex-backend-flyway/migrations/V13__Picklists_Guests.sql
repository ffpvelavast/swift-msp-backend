
DROP VIEW IF EXISTS rexbase.picklist_members;

DROP TABLE IF EXISTS rexbase.picklist_to_guest;
DROP TABLE IF EXISTS rexbase.picklist_to_beneficiary;
DROP TABLE IF EXISTS rexbase.picklist;
DROP TABLE IF EXISTS rexbase.guest;
DROP TABLE IF EXISTS rexbase.guest_type;

-----------------------------------------------------------------------------

DROP SEQUENCE IF EXISTS rexbase.picklist_id_seq;
DROP SEQUENCE IF EXISTS rexbase.guest_id_seq;

CREATE SEQUENCE rexbase.picklist_id_seq;
CREATE SEQUENCE rexbase.guest_id_seq;

-----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS rexbase.picklist
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval('rexbase.picklist_id_seq'::regclass),
    sortid smallint NOT NULL,
    displayname citext COLLATE pg_catalog."default" NOT NULL,
    rexuser_id bigint NOT NULL,
    CONSTRAINT picklist_pkey PRIMARY KEY (id),
    CONSTRAINT fk_picklist_rexuser FOREIGN KEY (rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS rexbase.picklist_to_beneficiary
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    beneficiary_id bigint NOT NULL,
    picklist_id bigint NOT NULL,
    CONSTRAINT picklist_to_beneficiary_pkey PRIMARY KEY (beneficiary_id, picklist_id),
    CONSTRAINT fk_picklist_to_beneficiary_beneficiary FOREIGN KEY (beneficiary_id)
        REFERENCES rexbase.beneficiary (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_picklist_to_beneficiary_picklist FOREIGN KEY (picklist_id)
        REFERENCES rexbase.picklist (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

------------------- guest tables --------------------

CREATE TABLE IF NOT EXISTS rexbase.guest_type
(
    id bigint NOT NULL,
    guest_enum text COLLATE pg_catalog."default" NOT NULL,
    display_text JSONB NOT NULL,
	description JSONB,
	business_guest boolean default true,
    CONSTRAINT guest_type_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS rexbase.guest
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval('rexbase.guest_id_seq'::regclass),
	platform_id bigint NOT NULL,
    organisationwide_guest boolean NOT NULL,
    default_guesttype_id bigint NOT NULL,
    creator_rexuser_id bigint NOT NULL,
    lastname citext COLLATE pg_catalog."default" NOT NULL,
    firstname citext COLLATE pg_catalog."default" NOT NULL,
    organization citext COLLATE pg_catalog."default" NOT NULL,
	jobtitle citext COLLATE pg_catalog."default" NOT NULL,
	taxation_abroad boolean NOT NULL DEFAULT FALSE,
    listname citext COLLATE pg_catalog."default" GENERATED ALWAYS AS (TRIM(BOTH FROM ((COALESCE((lastname)::text, '*unbekannt*'::text) || ', '::text) || COALESCE((firstname)::text, '*unbekannt*'::text)))) STORED,
	fullname citext COLLATE pg_catalog."default" GENERATED ALWAYS AS (TRIM(BOTH FROM ((COALESCE((firstname)::text, '*unbekannt*'::text) || ', '::text) || COALESCE((lastname)::text, '*unbekannt*'::text)))) STORED,
    CONSTRAINT guest_pkey PRIMARY KEY (id),
	CONSTRAINT fk_guest_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT fk_guest_to_rexuser FOREIGN KEY (creator_rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_guest_guest_type FOREIGN KEY (default_guesttype_id)
        REFERENCES rexbase.guest_type (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS rexbase.picklist_to_guest
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    guest_id bigint NOT NULL,
    picklist_id bigint NOT NULL,
    CONSTRAINT picklist_to_guest_pkey PRIMARY KEY (guest_id, picklist_id),
    CONSTRAINT fk_picklist_to_guest_guest FOREIGN KEY (guest_id)
        REFERENCES rexbase.guest (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_picklist_to_guest_picklist FOREIGN KEY (picklist_id)
        REFERENCES rexbase.picklist (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

---------------------------------------------------------------------------------
