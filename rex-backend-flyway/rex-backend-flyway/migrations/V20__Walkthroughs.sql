---------------------------------------------------------------------

DROP TABLE IF EXISTS rexbase.walkthroughs_read;
DROP TABLE IF EXISTS rexbase.walkthroughs;

DROP SEQUENCE IF EXISTS rexbase.walkthroughs_id_seq;

CREATE SEQUENCE rexbase.walkthroughs_id_seq;


----------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS rexbase.walkthroughs
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
	updated_at timestamp(6) with time zone,
	id bigint NOT NULL DEFAULT nextval('rexbase.walkthroughs_id_seq'::regclass),
	platform_id bigint NOT NULL,
	sortid bigint NOT NULL default 1,
	internal_title text not NULL,
	title jsonb,
	subtitle jsonb,
	description jsonb,
	picture_file text,
	activefrom date NOT NULL default '2020-01-01'::date,
	activethrough date NOT NULL default '2999-12-31'::date,
	activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, activethrough+1,'[)')) STORED,
    CONSTRAINT walkthroughs_pkey PRIMARY KEY (id),
	CONSTRAINT walkthroughs_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE IF NOT EXISTS rexbase.walkthroughs_read
(
    created_at timestamp(6) with time zone NOT NULL default CURRENT_TIMESTAMP(6),
    walkthrough_id bigint not NULL,
	rexuser_id bigint NOT NULL,
    CONSTRAINT walkthroughs_read_pkey PRIMARY KEY (walkthrough_id,rexuser_id),
	CONSTRAINT walkthroughs_read_to_rexuser FOREIGN KEY (rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

