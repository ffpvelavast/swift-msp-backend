DROP TABLE IF EXISTS taxation.taxation_run_history;
DROP TABLE IF EXISTS taxation.taxation_run_status;
DROP TABLE IF EXISTS taxation.taxation_run;

Drop Sequence IF EXISTS taxation.taxation_run_id_seq;
Drop Sequence IF EXISTS taxation.taxation_run_history_id_seq;
Drop Sequence IF EXISTS taxation.receipts_standalone_reporting_id_seq;

Create Sequence taxation.taxation_run_id_seq;
Create Sequence taxation.taxation_run_history_id_seq;
Create Sequence taxation.receipts_standalone_reporting_id_seq;

--------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS taxation.taxation_run_status
(
	created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
    id bigint NOT NULL,
	sortid smallint NOT NULL default 1,
	taxation_status_enum TEXT NOT NULL,
	display_text JSONB NOT NULL,
	CONSTRAINT taxation_run_status_pkey PRIMARY KEY (id),
	CONSTRAINT taxation_status_values
		CHECK (taxation_status_enum IN ('STARTED', 'FINALIZED','REVOKED'))
);


CREATE TABLE IF NOT EXISTS taxation.taxation_run
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	started_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	endded_at timestamp(6) with time zone,
    id bigint NOT NULL DEFAULT nextval('taxation.taxation_run_id_seq'::regclass),	
	initiator text NOT NULL,
    CONSTRAINT taxation_run_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS taxation.taxation_run_history
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    id bigint NOT NULL DEFAULT nextval('taxation.taxation_run_history_id_seq'::regclass),
    taxation_run_id bigint NOT NULL,
    taxation_run_status_id bigint NOT NULL,
	taxation_run_initiator citext NOT NULL,
    CONSTRAINT taxation_run_history_id PRIMARY KEY (id),
	CONSTRAINT fk_taxation_run_history_taxation_run FOREIGN KEY (taxation_run_id)
		REFERENCES taxation.taxation_run (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_taxation_run_history_taxation_run_status FOREIGN KEY (taxation_run_status_id)
        REFERENCES taxation.taxation_run_status (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

