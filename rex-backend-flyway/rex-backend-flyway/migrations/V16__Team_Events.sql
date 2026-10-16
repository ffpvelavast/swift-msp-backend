-------------------------------------------------------------------
----------------- data model for team events ----------------------
-------------------------------------------------------------------

DROP TABLE IF EXISTS events.team_event_to_beneficiary;
DROP TABLE IF EXISTS events.team_event_to_guest;

DROP TABLE IF EXISTS events.team_event_history;
DROP TABLE IF EXISTS events.team_event_approver;
DROP TABLE IF EXISTS events.team_event;
DROP TABLE IF EXISTS events.team_event_type;
DROP TABLE IF EXISTS events.team_event_status;

--------------------------------------------------------------------

DROP SEQUENCE IF EXISTS events.team_event_id_seq;
DROP SEQUENCE IF EXISTS events.team_event_history_id_seq;

CREATE SEQUENCE events.team_event_id_seq;
CREATE SEQUENCE events.team_event_history_id_seq;


----------------- team event status ---------------------------------
CREATE TABLE IF NOT EXISTS events.team_event_status
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    id smallint NOT NULL,
    display_text jsonb NOT NULL,
	status_code varchar(50) COLLATE pg_catalog."default",
    CONSTRAINT team_event_status_pkey PRIMARY KEY (id),
	CONSTRAINT receipt_status_code_check CHECK (((status_code)::text = ANY 
		((ARRAY['SUBMITTED'::character varying, 'CORRECTION_REQUESTED'::character varying, 'RESUBMITTED'::character varying, 
		'REJECTED'::character varying, 'APPROVED'::character varying, 'WITHDRAWN'::character varying,'PROCESSED'::character varying])::text[]))),
	CONSTRAINT receipt_status_code_unique UNIQUE (status_code)
);

----------------- team event type ---------------------------------
CREATE TABLE IF NOT EXISTS events.team_event_type
(
	created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
    id bigint NOT NULL,
	platform_id bigint NOT NULL,
	sortid smallint NOT NULL default 1,
	display_text JSONB NOT NULL,
	description JSONB NOT NULL,
	apply_law40_2 boolean NOT NULL DEFAULT FALSE,
	active boolean NOT NULL DEFAULT true,
	CONSTRAINT team_event_type_pkey PRIMARY KEY (id),
	CONSTRAINT team_event_type_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

----------------------- team event as such -------------------------
CREATE TABLE IF NOT EXISTS events.team_event
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
    --- later to be changed to separate table --- 
	processing_released_at timestamp(6) with time zone,
	processing_released_by text,
	processing_id bigint,
	id bigint NOT NULL DEFAULT nextval('events.team_event_id_seq'::regclass),
	platform_id bigint NOT NULL,
    rexuser_id bigint NOT NULL,
	projectnumber_id bigint,
	internal_note text COLLATE pg_catalog."default",
    event_title citext NOT NULL,
    event_date date NOT NULL,
	teameventtype_id bigint NOT NULL,
    bookable_through date NOT NULL,
    bookable_by text COLLATE pg_catalog."default" NOT NULL DEFAULT 'OWNER'::text,
    picklist_id bigint,
    CONSTRAINT team_event_pkey PRIMARY KEY (id),
	CONSTRAINT team_event_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT team_event_to_picklist FOREIGN KEY (picklist_id)
        REFERENCES rexbase.picklist (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT team_event_to_rexuser FOREIGN KEY (rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT team_event_to_team_event_type FOREIGN KEY (teameventtype_id)
        REFERENCES events.team_event_type (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT team_event_to_projectnumbers FOREIGN KEY (projectnumber_id)
        REFERENCES rexbase.projectnumbers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT bookable_type CHECK (bookable_by = ANY (ARRAY['OWNER'::text, 'PICKLIST'::text]))
);

----------------- team event approver -----------------------------
CREATE TABLE IF NOT EXISTS events.team_event_approver
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    teamevent_id bigint NOT NULL,
    rexuser_id bigint NOT NULL,
    CONSTRAINT teamevent_approver_pkey PRIMARY KEY (teamevent_id, rexuser_id),
    CONSTRAINT teamevent_approver_to_teamevent FOREIGN KEY (teamevent_id)
        REFERENCES events.team_event (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT teamevent_approver_to_rexuser FOREIGN KEY (rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

----------------- team event history -----------------------------
CREATE TABLE IF NOT EXISTS events.team_event_history
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	id bigint NOT NULL DEFAULT nextval('events.team_event_history_id_seq'::regclass),
    teamevent_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    status_id smallint NOT NULL,
    external_note text COLLATE pg_catalog."default",
    active boolean NOT NULL DEFAULT false,
    CONSTRAINT team_event_history_pkey PRIMARY KEY (id),
    CONSTRAINT team_event_history_to_teamvent FOREIGN KEY (teamevent_id)
        REFERENCES events.team_event (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT team_event_history_to_team_event_status FOREIGN KEY (status_id)
        REFERENCES events.team_event_status (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT team_event_history_to_rexuser FOREIGN KEY (creator_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-- DROP INDEX IF EXISTS events.one_active_team_event_history;

CREATE UNIQUE INDEX IF NOT EXISTS one_active_team_event_history
    ON events.team_event_history USING btree
    (teamevent_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default
    WHERE active = true;
	
-----------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS events.team_event_to_beneficiary
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    teamevent_id bigint NOT NULL,
    beneficiary_id bigint NOT NULL,
    CONSTRAINT teamevent_to_beneficiary_pkey PRIMARY KEY (teamevent_id, beneficiary_id),
    CONSTRAINT teamevent_to_beneficiary_beneficiary FOREIGN KEY (beneficiary_id)
        REFERENCES rexbase.beneficiary (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT teamevent_to_beneficiary_teamevent FOREIGN KEY (teamevent_id)
        REFERENCES events.team_event (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE IF NOT EXISTS events.team_event_to_guest
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    teamevent_id bigint NOT NULL,
    guest_id bigint NOT NULL,
    CONSTRAINT teamevent_to_guest_pkey PRIMARY KEY (teamevent_id, guest_id),
    CONSTRAINT teamevent_to_guest_guest FOREIGN KEY (guest_id)
        REFERENCES rexbase.guest (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT teamevent_to_guest_teamevent FOREIGN KEY (teamevent_id)
        REFERENCES events.team_event (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
