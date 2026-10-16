
----------- Datamodel for providing beautified Email-Services --------

DROP TABLE IF EXISTS rexbase.email_accounts;
DROP TABLE IF EXISTS rexbase.email_design;

DROP SEQUENCE IF EXISTS rexbase.email_accounts_id_seq;
DROP SEQUENCE IF EXISTS rexbase.email_design_id_seq;

CREATE SEQUENCE rexbase.email_accounts_id_seq;
CREATE SEQUENCE rexbase.email_design_id_seq;



CREATE TABLE IF NOT EXISTS rexbase.email_accounts
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval('rexbase.email_accounts_id_seq'::regclass),
	account_name text NOT NULL,
	account_user text COLLATE pg_catalog."default" NOT NULL,
    account_passwort text COLLATE pg_catalog."default" NOT NULL,
    sender_name text COLLATE pg_catalog."default" NOT NULL,
    sender_address text COLLATE pg_catalog."default" NOT NULL,
    email_server text COLLATE pg_catalog."default" NOT NULL,
    email_port smallint NOT NULL DEFAULT 465,
    email_ssl boolean NOT NULL DEFAULT true,
    CONSTRAINT email_account_pkey PRIMARY KEY (id)
);
	
CREATE TABLE IF NOT EXISTS rexbase.email_design
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
    id bigint NOT NULL DEFAULT nextval('rexbase.email_design_id_seq'::regclass),
    design_name text COLLATE pg_catalog."default" NOT NULL,
    mail_header text COLLATE pg_catalog."default" NOT NULL,
    mail_footer text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT email_design_pkey PRIMARY KEY (id)
);
