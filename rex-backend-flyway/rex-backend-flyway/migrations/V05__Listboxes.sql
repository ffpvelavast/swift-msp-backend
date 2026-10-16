-- Table: rexbase.listboxes
DROP TABLE IF EXISTS rexbase.listboxes;
DROP TABLE IF EXISTS rexbase.listboxes_client;

CREATE TABLE IF NOT EXISTS rexbase.listboxes
(
	created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP(6),
	updated_at timestamp without time zone,
	screen_code text NOT NULL,
	listbox_code text NOT NULL,
    listbox_enum text NOT NULL,
	sortid smallint NOT NULL default 0,
    display_text jsonb NOT NULL,
    description jsonb,
    CONSTRAINT listboxes_pkey PRIMARY KEY (listbox_enum)
);

-------------------------------------------------------
-- example
-- screen_code: event_details
-- listbox_code: inheritance_mode
-- listbox_enum: FORCED, OPTIONAL, NOT_PERMITTED
-------------------------------------------------------


CREATE TABLE IF NOT EXISTS rexbase.listboxes_client
(
	created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP(6),
	updated_at timestamp without time zone,
    platform_id bigint NOT NULL,
	screen_code text NOT NULL,
	listbox_code text NOT NULL,
    listbox_enum text NOT NULL,
	sortid smallint NOT NULL default 0,
    display_text jsonb NOT NULL,
    description jsonb,
    CONSTRAINT listboxes_client_pkey PRIMARY KEY (platform_id,listbox_enum),
	CONSTRAINT listboxes_client_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

