DROP TABLE IF EXISTS rexbase.translations_client;
DROP TABLE IF EXISTS rexbase.translations;

DROP SEQUENCE IF EXISTS rexbase.translations_id_seq;
DROP SEQUENCE IF EXISTS rexbase.translations_client_id_seq;

CREATE SEQUENCE rexbase.translations_id_seq;
CREATE SEQUENCE rexbase.translations_client_id_seq;


------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS rexbase.translations
(
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP(6),
	updated_at timestamp without time zone,
    id bigint NOT NULL DEFAULT nextval('rexbase.translations_id_seq'::regclass),
    key text NOT NULL,
    component text NOT NULL,
    translations jsonb NOT NULL,
    same_content_id bigint,
    CONSTRAINT translations_pkey PRIMARY KEY (id),
    CONSTRAINT translations_component_key_key UNIQUE (component, key)
);


CREATE TABLE IF NOT EXISTS rexbase.translations_client
(
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP(6),
	updated_at timestamp without time zone,
    id bigint NOT NULL DEFAULT nextval('rexbase.translations_client_id_seq'::regclass),
    platform_id bigint NOT NULL,
    key text NOT NULL,
    component text NOT NULL,
    translations jsonb NOT NULL,
    samecontent_id bigint,
    CONSTRAINT translations_client_pkey PRIMARY KEY (id),
    CONSTRAINT translations_client_component_key_fk UNIQUE (component, key),
    CONSTRAINT translations_client_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
