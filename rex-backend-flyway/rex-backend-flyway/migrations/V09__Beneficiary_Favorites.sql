DROP TABLE IF EXISTS rexbase.beneficiary_favorites;

CREATE TABLE IF NOT EXISTS rexbase.beneficiary_favorites
(
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	id bigint NOT NULL,
    sortid smallint NOT NULL,
    rexuser_id bigint NOT NULL,
    beneficiary_id bigint NOT NULL,
    CONSTRAINT beneficiary_favorites_pkey PRIMARY KEY (id),
    CONSTRAINT fk_beneficiary_favorites_to_beneficiary FOREIGN KEY (beneficiary_id)
        REFERENCES rexbase.beneficiary (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_beneficiary_favorites_to_rexuser FOREIGN KEY (rexuser_id)
        REFERENCES rexbase.rexuser (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);