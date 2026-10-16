
DROP SCHEMA IF EXISTS odbc CASCADE;
CREATE SCHEMA IF NOT EXISTS odbc AUTHORIZATION postgres;

---- not neede because for CASCADE on Schema
-- DROP VIEW IF EXISTS odbc.translations;
-- DROP VIEW IF EXISTS odbc.translations_client;
-- DROP VIEW IF EXISTS odbc.listboxes;
-- DROP VIEW IF EXISTS odbc.listboxes_client;

----------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS odbc.server_identifier
(
    servername text,
    CONSTRAINT server_identifier_pkey PRIMARY KEY (servername)
);


COMMENT ON TABLE odbc.server_identifier
    IS 'This table is to support ODBC access and to provide a security mechanismen against faulty data manipulation. By reading the value of servername prior to any write database access the support application (VBA REX-Suite) can check whether it is communicating with the intended server.';

INSERT INTO odbc.server_identifier(servername) values('REX_MASTER_JENS');

-----======================== label translations ==================================

CREATE OR REPLACE VIEW odbc.translations
 AS
 SELECT created_at,
    updated_at,
    id,
    key,
    component,
    translations::text AS translations,
    translations ->> 'de'::text AS label_de,
    translations ->> 'en'::text AS label_en,
    same_content_id
   FROM rexbase.translations;
   
 ------------------------------------------------------------------
   

CREATE OR REPLACE VIEW odbc.translations_client
 AS
 SELECT created_at,
    updated_at,
    id,
    key,
    component,
    translations::text AS translations,
    translations ->> 'de'::text AS label_de,
    translations ->> 'en'::text AS label_en
   FROM rexbase.translations_client;
   
 
-----======================== listbox translations ================================== 
 

CREATE OR REPLACE VIEW odbc.listboxes
 AS
 SELECT created_at,
    updated_at,
    screen_code,
	listbox_code,
	listbox_enum,
	sortid,
    display_text::text AS display_text,
    display_text ->> 'de'::text AS display_text_de,
    display_text ->> 'en'::text AS display_text_en,
	description::text AS description,
    description ->> 'de'::text AS description_de,
    description ->> 'en'::text AS description_en
FROM rexbase.listboxes;


CREATE OR REPLACE VIEW odbc.listboxes_client
 AS
 SELECT created_at,
    updated_at,
    screen_code,
	listbox_code,
	listbox_enum,
	sortid,
    display_text::text AS display_text,
    display_text ->> 'de'::text AS display_text_de,
    display_text ->> 'en'::text AS display_text_en,
	description::text AS description,
    description ->> 'de'::text AS description_de,
    description ->> 'en'::text AS description_en
FROM rexbase.listboxes_client;