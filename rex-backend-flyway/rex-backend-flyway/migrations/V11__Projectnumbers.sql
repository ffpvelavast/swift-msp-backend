DROP TABLE IF EXISTS rexbase.projectleader;
DROP TABLE IF EXISTS rexbase.projectnumbers;


DROP SEQUENCE IF EXISTS rexbase.projectnumbers_id_seq;
DROP SEQUENCE IF EXISTS rexbase.projectleader_id_seq;

CREATE SEQUENCE rexbase.projectnumbers_id_seq;
CREATE SEQUENCE rexbase.projectleader_id_seq;


--------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS rexbase.projectnumbers
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval('rexbase.projectnumbers_id_seq'::regclass),
    platform_id bigint NOT NULL,
    projectnumberid uuid NOT NULL,
    external_projectid text,
    projectnumber text NOT NULL,
    projecttitle citext NOT NULL,
    projectsubtitle citext,
    description citext,
    activefrom date DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT projectnumber_pkey PRIMARY KEY (id),
    CONSTRAINT projectnumbers_platform_id_projectnumber_key UNIQUE (platform_id, projectnumber),
    CONSTRAINT projectnumber_platform_id_fkey FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS rexbase.projectleader
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
    id bigint NOT NULL DEFAULT nextval('rexbase.projectleader_id_seq'::regclass),
    projectnumber_id bigint NOT NULL,
    rexuser_id bigint NOT NULL,
    activefrom date DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT projectleader_pkey PRIMARY KEY (id),
    CONSTRAINT projectleader_projectnumber_fkey FOREIGN KEY (projectnumber_id)
        REFERENCES rexbase.projectnumbers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT projectleader_rexuser_fkey FOREIGN KEY (rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT projectleader_activeperiod EXCLUDE USING gist (projectnumber_id WITH =,rexuser_id WITH =,activeperiod WITH &&)
);


CREATE INDEX IF NOT EXISTS idx_projectnumbers_platform_activeperiod_gist
ON rexbase.projectnumbers
USING gist (platform_id, activeperiod);

CREATE INDEX idx_projectnumbers_projectnumber_trgm
ON rexbase.projectnumbers
USING gin (projectnumber gin_trgm_ops);

CREATE INDEX idx_projectnumbers_projecttitle_trgm
ON rexbase.projectnumbers
USING gin ((projecttitle::text) gin_trgm_ops);

CREATE INDEX idx_projectnumbers_projectsubtitle_trgm
ON rexbase.projectnumbers
USING gin ((projectsubtitle::text) gin_trgm_ops);
