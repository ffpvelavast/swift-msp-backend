DROP TABLE IF EXISTS masterdata.corporate_unit;
DROP TABLE IF EXISTS masterdata.corporate_division;


DROP SEQUENCE IF EXISTS masterdata.corporate_division_id_seq;
DROP SEQUENCE IF EXISTS masterdata.corporate_unit_id_seq;

CREATE SEQUENCE masterdata.corporate_division_id_seq;
CREATE SEQUENCE masterdata.corporate_unit_id_seq;

------------------------ Table corporate_division --------------------------
CREATE TABLE IF NOT EXISTS masterdata.corporate_division
(
    created_at    timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at    timestamp(6) with time zone,
    id            bigint                      NOT NULL DEFAULT nextval('masterdata.corporate_division_id_seq'::regclass),
    sortid        integer,
    platform_id   bigint                      NOT NULL,
    division_name JSONB                       NOT NULL,
    activefrom    date                        NOT NULL DEFAULT '2020-01-01'::date,
    activethrough date                        NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod  daterange GENERATED ALWAYS AS (daterange(activefrom, activethrough + 1, '[)')) STORED,
    CONSTRAINT corporate_division_pkey PRIMARY KEY (id),
    CONSTRAINT corporate_division_name_uq UNIQUE (division_name),
    CONSTRAINT corporate_division_to_platform FOREIGN KEY (platform_id)
        REFERENCES rexbase.platform (id) MATCH SIMPLE
        ON UPDATE NO ACTION
);

-------------------------- Table corporate_unit --------------------------
CREATE TABLE IF NOT EXISTS masterdata.corporate_unit
(
    created_at            timestamp(6) with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at            timestamp(6) with time zone,
    id                    bigint                      NOT NULL DEFAULT nextval('masterdata.corporate_unit_id_seq'::regclass),
    sortid                integer,
    corporate_division_id bigint                      NOT NULL,
    unit_name             JSONB                       NOT NULL,
    activefrom            date                        NOT NULL DEFAULT '2020-01-01'::date,
    activethrough         date                        NOT NULL DEFAULT '2999-12-31'::date,
    activeperiod          daterange GENERATED ALWAYS AS (daterange(activefrom, activethrough + 1, '[)')) STORED,
    CONSTRAINT corporate_unit_pkey PRIMARY KEY (id),
    CONSTRAINT corporate_unit_name_uq UNIQUE (unit_name, corporate_division_id),
    CONSTRAINT corporate_unit_to_divison FOREIGN KEY (corporate_division_id)
        REFERENCES masterdata.corporate_division (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
