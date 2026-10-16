DROP TABLE IF EXISTS rexbase.deputy_right_assignment;
DROP TABLE IF EXISTS rexbase.deputy_assignment;
DROP TABLE IF EXISTS rexbase.deputy_right;

-------------------------------------------------

DROP SEQUENCE IF EXISTS rexbase.deputy_right_id_seq;
CREATE SEQUENCE rexbase.deputy_right_id_seq;

-----------------------------------------------------
CREATE TABLE IF NOT EXISTS rexbase.deputy_right
(
	id bigint NOT NULL DEFAULT nextval('rexbase.deputy_right_id_seq'::regclass),
    deputyright_enum text NOT NULL,
	display_text JSONB NOT NULL,
    CONSTRAINT deputyright_pkey PRIMARY KEY (id),
	CONSTRAINT deputyright_enum UNIQUE (deputyright_enum)
);

CREATE TABLE IF NOT EXISTS rexbase.deputy_assignment
(
    created_at timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at timestamp(6) with time zone,
    id bigint NOT NULL,
    rexuser_id bigint NOT NULL,
    deputy_id bigint NOT NULL,
    activefrom date NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod daterange GENERATED ALWAYS AS (daterange(activefrom, (activethrough + 1), '[)'::text)) STORED,
    CONSTRAINT deputy_assignment_pkey PRIMARY KEY (id),
    CONSTRAINT fk_deputy_assignment_beneficiary FOREIGN KEY (rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_deputy_assignment_deputy FOREIGN KEY (deputy_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS rexbase.deputy_right_assignment
(
    deputy_assignment_id bigint NOT NULL,
    deputyright_id bigint NOT NULL,
    CONSTRAINT deputyright_assignment_pkey PRIMARY KEY (deputy_assignment_id, deputyright_id),
    CONSTRAINT fk_deputyright_assignment_deputy_assignment FOREIGN KEY (deputy_assignment_id)
        REFERENCES rexbase.deputy_assignment (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_deputyright_assignment_deputyright FOREIGN KEY (deputyright_id)
        REFERENCES rexbase.deputy_right (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);