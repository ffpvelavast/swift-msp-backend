
DROP VIEW IF EXISTS rexbase.rexuser_roles;
DROP VIEW IF EXISTS rexbase.rexuser_rights;

DROP TABLE IF EXISTS rexbase.userrole_to_right;
DROP TABLE IF EXISTS rexbase.rexuser_to_role;
DROP TABLE IF EXISTS rexbase.userright;
DROP TABLE IF EXISTS rexbase.userrole;

DROP TABLE IF EXISTS rexbase.rexuser;

-----------------------------------------------------------------------------------------------

DROP SEQUENCE IF EXISTS rexbase.rexuser_id_seq;
DROP SEQUENCE IF EXISTS rexbase.userrole_id_seq;
DROP SEQUENCE IF EXISTS rexbase.userright_id_seq;


CREATE SEQUENCE rexbase.rexuser_id_seq;
CREATE SEQUENCE rexbase.userrole_id_seq;
CREATE SEQUENCE rexbase.userright_id_seq;

-----------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS rexbase.rexuser
(
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp with time zone,	
	id bigint NOT NULL DEFAULT nextval('rexbase.rexuser_id_seq'::regclass),
	platform_id bigint NOT NULL,
    keycloakid uuid,
    lastname citext COLLATE pg_catalog."default",
    firstname citext COLLATE pg_catalog."default",
    academictitle text COLLATE pg_catalog."default",
	listname citext COLLATE pg_catalog."default" GENERATED ALWAYS AS (TRIM(BOTH FROM ((COALESCE((lastname)::text, '*unbekannt*'::text) || ', '::text) || btrim(((COALESCE((academictitle)::text, ''::text) || ' '::text) || COALESCE((firstname)::text, '*unbekannt*'::text)))))) STORED,
	fullname citext GENERATED ALWAYS AS (btrim(COALESCE(academictitle,'') || ' ' || COALESCE(firstname, '*unbekannt*') ||  ' ' || COALESCE(lastname,'*unbekannt*'))) STORED,
    emailaddress citext COLLATE pg_catalog."default",
	locale char(2) NOT NULL DEFAULT 'de',
    active boolean NOT NULL DEFAULT false,
	activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT rexuser_pkey PRIMARY KEY (id),
    CONSTRAINT fk_rexuser_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS rexbase.userrole
(
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	id bigint NOT NULL DEFAULT nextval('rexbase.userrole_id_seq'::regclass),
	role_enum text COLLATE pg_catalog."default" NOT NULL,
    display_text jsonb NOT NULL DEFAULT '{}'::jsonb,
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT userrole_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS rexbase.userright
(
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	id bigint NOT NULL DEFAULT nextval('rexbase.userright_id_seq'::regclass),
	right_enum text COLLATE pg_catalog."default" NOT NULL,
 	display_text jsonb NOT NULL DEFAULT '{}'::jsonb,
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT userright_pkey PRIMARY KEY (id)
);

-----------------------------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS rexbase.rexuser_to_role
(
	created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	updated_at timestamp with time zone,
    rexuser_id bigint NOT NULL,
    userrole_id bigint NOT NULL,
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT rexuser_to_role_pkey PRIMARY KEY (rexuser_id, userrole_id, activefrom),
    CONSTRAINT fk_rexuser_to_role_rexuser FOREIGN KEY (rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_rexuser_to_role_userrole FOREIGN KEY (userrole_id)
        REFERENCES rexbase.userrole (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS rexbase.userrole_to_right
(
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    userrole_id bigint NOT NULL,
    userright_id bigint NOT NULL,
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT userrole_to_right_pkey PRIMARY KEY (userrole_id, userright_id),
    CONSTRAINT fk_userrole_to_right_userright FOREIGN KEY (userright_id)
        REFERENCES rexbase.userright (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_userrole_to_right_userrole FOREIGN KEY (userrole_id)
        REFERENCES rexbase.userrole (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

---------------------------- VIEWS ---------------------------------------

--------------------------- VIEWS ---------------------------------------

CREATE OR REPLACE VIEW rexbase.rexuser_roles
 AS
 WITH rolescomplete AS (
         SELECT rexusr.id AS rexuser_id,
            rexusr.listname,
			roles.role_enum,
            roles.display_text,
            rexusr.activeperiod @> CURRENT_DATE AS user_active,
            roles.activeperiod @> CURRENT_DATE AS role_active,
            user_role.activeperiod @> CURRENT_DATE AS userrole_active
           FROM rexbase.rexuser rexusr
             JOIN rexbase.rexuser_to_role user_role ON user_role.rexuser_id = rexusr.id
             JOIN rexbase.userrole roles ON roles.id = user_role.userrole_id
        )
 SELECT DISTINCT rexuser_id,
    listname,
    role_enum,
	display_text
   FROM rolescomplete
  WHERE user_active AND role_active AND userrole_active;

-----------------------------------------------------------------------------------

CREATE OR REPLACE VIEW rexbase.rexuser_rights
 AS
 WITH rightscomplete AS (
         SELECT rexusr.id AS rexuser_id,
            rexusr.listname,
			rights.right_enum,
			rights.display_text,
            rexusr.activeperiod @> CURRENT_DATE AS user_active,
            roles.activeperiod @> CURRENT_DATE AS role_active,
            rights.activeperiod @> CURRENT_DATE AS right_active,
            role_rights.activeperiod @> CURRENT_DATE AS roleright_active,
            user_role.activeperiod @> CURRENT_DATE AS userrole_active
           FROM rexbase.rexuser rexusr
             JOIN rexbase.rexuser_to_role user_role ON user_role.rexuser_id = rexusr.id
             JOIN rexbase.userrole_to_right role_rights ON role_rights.userrole_id = user_role.userrole_id
             JOIN rexbase.userright rights ON rights.id = role_rights.userright_id
             JOIN rexbase.userrole roles ON roles.id = role_rights.userrole_id
        )
 SELECT DISTINCT rexuser_id,
    listname,
    right_enum,
	display_text
   FROM rightscomplete
  WHERE user_active AND role_active AND right_active AND roleright_active AND userrole_active;
